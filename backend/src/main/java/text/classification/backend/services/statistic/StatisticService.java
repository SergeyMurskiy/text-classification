package text.classification.backend.services.statistic;

import text.classification.backend.models.statistic.Statistic;
import text.classification.backend.repositories.StatisticRepository;
import text.classification.common.services.RequestService;
import text.classification.common.services.classifier.ClassifierRequest;
import text.classification.common.services.classifier.ClassifierResponse;
import text.classification.common.services.classifier.ClassifierResponseEntity;
import text.classification.common.services.classifier.ClassifierService;
import text.classification.common.services.learning.LearningRequest;
import text.classification.common.services.learning.LearningService;
import text.classification.common.services.reader.TextReaderFacade;
import text.classification.backend.services.weka.WekaDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StatisticService {
    @Value("${text.to.load.folder}")
    private String textFolderPath;
    @Autowired
    private RequestService requestService;

    private final List<ClassifierService> classifierServiceList;
    private final List<LearningService> learningServiceList;

    private final WekaDataService wekaDataService;

    private final TextReaderFacade textReader;

    private final StatisticRepository statisticRepository;

    @Autowired
    public StatisticService(List<ClassifierService> classifierServiceList, WekaDataService wekaDataService,
                            TextReaderFacade textReader, StatisticRepository statisticRepository,
                            List<LearningService> learningServiceList) {
        this.classifierServiceList = classifierServiceList;
        this.wekaDataService = wekaDataService;
        this.textReader = textReader;
        this.statisticRepository = statisticRepository;
        this.learningServiceList = learningServiceList;
    }

    static class Counter {
        static int countOfTexts = 0;
    }

    public List<String> getAllMethodsName() {
        return classifierServiceList.stream()
                .map(ClassifierService::getMethodName)
                .collect(Collectors.toList());
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

        Counter.countOfTexts = 0;

        Map<String, List<File>> textsFromFolder = getTextsFromFolder();
        Map<String, List<File>> etalons = getEtalonsFromFolder();

        Map<String, List<ClassifierRequest>> classifierRequestMap = createClassifierRequests(etalons);
        Map<String, List<LearningRequest>> learningRequestMap = createLearningRequests(textsFromFolder);

        String pattern = "MM-dd-yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String date = simpleDateFormat.format(new Date());

        List<String> topics = new ArrayList<>(textsFromFolder.keySet());
//        wekaDataService.buildWekaDataModel(("statistic " + sizeOfAttributes + date), textModelService.createAttributesByTFIDF(textsFromFolder, sizeOfAttributes / 3), topics, true);
        wekaDataService.buildWekaDataModel(("statistic " + sizeOfAttributes + date), sizeOfAttributes, topics, true);


        long start = System.currentTimeMillis();
        for (int i = 0; i < sizeOfTexts; i++) {
            for (String topic : topics) {
                LearningRequest learningRequest = learningRequestMap.get(topic).get(i);

                learningServiceList.forEach(learningService -> learningService.learning(Collections.singletonList(learningRequest), true));
            }

            Counter.countOfTexts++;

            List<Statistic> collect = classifierServiceList.parallelStream()
                    .map(classifierService -> classifierRequestMap.entrySet().parallelStream()
                            .map(entry -> {
                                ClassifierResponse classifierResponse = classifierService.classifier(entry.getValue());
                                return classifierResponse.getResponseEntities().parallelStream()
                                        .map(classifierResponseEntity -> createStatistic(classifierService.getMethodName(), entry.getKey(), classifierResponseEntity))
                                        .collect(Collectors.toList());
                            }).collect(Collectors.toList()))
                    .flatMap(List::stream)
                    .flatMap(List::stream)
                    .collect(Collectors.toList());

            statisticRepository.saveAll(collect);
        }

        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }

    private Statistic createStatistic(String methodName, String topic, ClassifierResponseEntity classifierResponseEntity) {
        String status;
        if (topic.equals(classifierResponseEntity.getClassValue())) {
            status = "correct";
        } else {
            status = "incorrect";
        }
        return Statistic.builder()
                .date(new Date())
                .methodName(methodName)
                .countOfTexts(Counter.countOfTexts)
                .topic(topic)
                .status(status)
                .time(classifierResponseEntity.getTime())
                .build();
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

    private Map<String, List<ClassifierRequest>> createClassifierRequests(Map<String, List<File>> etalonTextsByTopic) {
        Map<String, List<ClassifierRequest>> result = new HashMap<>();
        etalonTextsByTopic.forEach((topic, textList) -> {
            List<ClassifierRequest> classifierRequests = textList.stream()
                    .map(textFile -> requestService.createClassifierRequest(textFile, "UTF-8"))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList());

            result.put(topic, classifierRequests);
        });

        return result;
    }

    private Map<String, List<LearningRequest>> createLearningRequests(Map<String, List<File>> textToLearnByTopic) {
        Map<String, List<LearningRequest>> result = new HashMap<>();
        textToLearnByTopic.forEach((topic, textList) -> {
            List<LearningRequest> classifierRequests = textList.stream()
                    .map(textFile -> requestService.createLearningRequest(textFile, topic, "UTF-8"))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList());

            result.put(topic, classifierRequests);
        });

        return result;
    }
}
