package murskiy_sergey.graduate_work.services.statistic;

import murskiy_sergey.graduate_work.config.WekaClassifiers;
import murskiy_sergey.graduate_work.models.statistic.Statistic;
import murskiy_sergey.graduate_work.repositories.StatisticRepository;
import murskiy_sergey.graduate_work.services.TextModelService;
import murskiy_sergey.graduate_work.services.analyze.AnalyzeResponse;
import murskiy_sergey.graduate_work.services.analyze.AnalyzeResponseEntity;
import murskiy_sergey.graduate_work.services.analyze.AnalyzeService;
import murskiy_sergey.graduate_work.services.learning.LearningResponse;
import murskiy_sergey.graduate_work.services.learning.elasticsearch.ElasticsearchLearningService;
import murskiy_sergey.graduate_work.services.learning.weka.WekaLearningService;
import murskiy_sergey.graduate_work.services.reader.TextReaderFacade;
import murskiy_sergey.graduate_work.services.weka.WekaDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class StatisticService {
    @Value("${text.to.load.folder}")
    private String textFolderPath;

    @Autowired
    private List<AnalyzeService> analyzeServiceList;

    @Autowired
    private TextModelService textModelService;

    @Autowired
    private WekaDataService wekaDataService;

    @Autowired
    private WekaClassifiers wekaClassifiers;

    @Autowired
    private WekaLearningService wekaLearningService;
    @Autowired
    private ElasticsearchLearningService elasticsearchLearningService;

    @Autowired
    private TextReaderFacade textReader;

    @Autowired
    private StatisticRepository statisticRepository;

    static class Counter {
        static int countOfTexts = 0;
        static int countOfWords = 0;
    }

    public void deleteAllStatistic() {
        statisticRepository.deleteAll();
    }

    public List<Statistic> getStatistic() {
        Iterable<Statistic> statisticRepositoryAll = statisticRepository.findAll();
        List<Statistic> statisticList = new ArrayList<>();
        statisticRepositoryAll.forEach(statisticList::add);

        return statisticList;
    }

    public void generateStatistic(int sizeOfTexts, int sizeOfAttributes) {
        statisticRepository.deleteAll();

        Counter.countOfWords = 0;
        Counter.countOfTexts = 0;

        Map<String, List<File>> textsFromFolder = getTextsFromFolder();
        Map<String, List<File>> etalons = getEtalonsFromFolder();

        String pattern = "MM-dd-yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String date = simpleDateFormat.format(new Date());

        List<String> topics = new ArrayList<>(textsFromFolder.keySet());
//        wekaDataService.buildWekaDataModel(("statistic " + sizeOfAttributes + date), textModelService.createAttributesByTFIDF(textsFromFolder, sizeOfAttributes / 3), topics, true);
        wekaDataService.buildWekaDataModel(("statistic " + sizeOfAttributes + date), sizeOfAttributes, topics, true);

        long start = System.currentTimeMillis();
        for (int i = 0; i < sizeOfTexts; i++) {
            for (String topic : topics) {
                File text = textsFromFolder.get(topic).get(i);

                Optional<MultipartFile> result = convertFileToMultipartFile(text);

                if (!result.isPresent()) {
                    break;
                }

                LearningResponse learningResponse = wekaLearningService.learning(new MultipartFile[]{result.get()}, topic, "UTF-8", true);
                elasticsearchLearningService.learning(new MultipartFile[]{result.get()}, topic, "UTF-8", true);
                Counter.countOfWords += learningResponse.getCountOfWords();
            }

            Counter.countOfTexts++;
            List<Statistic> collect = analyzeServiceList.parallelStream()
                    .map(analyzeService -> etalons.entrySet().parallelStream()
                            .map(entry -> {
                                AnalyzeResponse analyzeResponse = analyzeService.analyze(getMultipartFiles(entry.getValue()), "UTF-8");
                                return analyzeResponse.getAnalyzeResponseEntities().parallelStream()
                                        .map(analyzeResponseEntity -> createStatistic(analyzeService.getName(), entry.getKey(), analyzeResponseEntity))
                                        .collect(Collectors.toList());
                            }).collect(Collectors.toList()))
                    .flatMap(List::stream)
                    .flatMap(List::stream)
                    .collect(Collectors.toList());

//            List<AnalyzeThread> analyzeThreadList = analyzeServiceList
//                    .stream()
//                    .map(analyzeService -> etalons.entrySet()
//                            .stream()
//                            .map(entry -> {
//                                AnalyzeThread analyzeThread = new AnalyzeThread(analyzeService, getMultipartFiles(entry.getValue()), entry.getKey(), "UTF-8");
//                                analyzeThread.start();
//                                return analyzeThread;
//                            }).collect(Collectors.toList()))
//                    .flatMap(List::stream)
//                    .collect(Collectors.toList());
//
//            analyzeThreadList.forEach(analyzeThread -> {
//                try {
//                    analyzeThread.join();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            });
//
//            List<Statistic> collect = analyzeThreadList.stream()
//                    .map(AnalyzeThread::getStatistic)
//                    .flatMap(List::stream)
//                    .collect(Collectors.toList());
            statisticRepository.saveAll(collect);
        }

        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }

    private Statistic createStatistic(String methodName, String topic, AnalyzeResponseEntity analyzeResponseEntity) {
        String status;
        if (topic.equals(analyzeResponseEntity.getClassValue())) {
            status = "correct";
        } else {
            status = "incorrect";
        }
        return new Statistic(UUID.randomUUID().toString(), new Date(), methodName, Counter.countOfTexts, Counter.countOfWords,
                topic, status, analyzeResponseEntity.getMilliseconds());
    }

    private MultipartFile[] getMultipartFiles(List<File> fileList) {
        return fileList.stream()
                .map(this::convertFileToMultipartFile)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toArray(MultipartFile[]::new);
    }

    private Optional<MultipartFile> convertFileToMultipartFile(File file) {
        String name = file.getName().replace("docx", "txt");
        String originalFileName = file.getName().replace("docx", "txt");
        String contentType = "text/plain";

        Optional<String> fileContent = textReader.getTextContent(file, "UTF-8");
        return fileContent.map(s -> new MockMultipartFile(name, originalFileName, contentType, s.getBytes()));

    }

    private Map<String, List<File>> getTextsFromFolder() {
        File folder = new File(textFolderPath + "/learn");
        return getFilesFromFolder(folder);
    }

    private Map<String, List<File>> getEtalonsFromFolder() {
        File etalonsFolder = new File(textFolderPath + "/etalons");
        Map<String, List<File>> filesFromFolder = getFilesFromFolder(etalonsFolder);
        filesFromFolder.forEach((key, value) -> filesFromFolder.put(key, value.subList(0, 33)));
        return filesFromFolder;
    }

    private Map<String, List<File>> getFilesFromFolder(File folder) {
        File[] topicFolders = folder.listFiles((current, name) -> new File(current, name).isDirectory());

        Map<String, List<File>> result = new HashMap<>();

        Arrays.stream(topicFolders != null ? topicFolders : new File[0])
                .forEach(topicFolder -> {
                    File[] texts = topicFolder.listFiles((current, name) -> !new File(current, name).isDirectory());

                    if (texts != null) {
                        result.put(topicFolder.getName(), Arrays.asList(texts));
                    }
                });

        return result;
    }
}
