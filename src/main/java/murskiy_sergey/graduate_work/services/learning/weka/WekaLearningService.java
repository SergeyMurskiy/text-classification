package murskiy_sergey.graduate_work.services.learning.weka;

import murskiy_sergey.graduate_work.config.WekaClassifiers;
import murskiy_sergey.graduate_work.models.weka.data.WekaDataEntity;
import murskiy_sergey.graduate_work.models.weka.data.WekaDataModel;
import murskiy_sergey.graduate_work.repositories.WekaDataEntityRepository;
import murskiy_sergey.graduate_work.repositories.WekaDataModelRepository;
import murskiy_sergey.graduate_work.services.TextAnalyzer;
import murskiy_sergey.graduate_work.services.learning.LearningResponse;
import murskiy_sergey.graduate_work.services.learning.LearningResponseEntity;
import murskiy_sergey.graduate_work.services.learning.LearningService;
import murskiy_sergey.graduate_work.services.reader.TextReaderFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class WekaLearningService implements LearningService {
    private final TextReaderFacade textReader;
    private final TextAnalyzer textAnalyzer;
    private final WekaDataModelRepository wekaDataModelRepository;
    private final WekaClassifiers wekaClassifiers;
    private final WekaDataEntityRepository wekaDataEntityRepository;

    private WekaDataModel wekaDataModel;

    @Autowired
    public WekaLearningService(TextReaderFacade textReader, TextAnalyzer textAnalyzer,
                               WekaDataModelRepository wekaDataModelRepository,
                               WekaClassifiers wekaClassifiers,
                               WekaDataEntityRepository wekaDataEntityRepository) {
        this.textReader = textReader;
        this.textAnalyzer = textAnalyzer;
        this.wekaDataModelRepository = wekaDataModelRepository;
        this.wekaClassifiers = wekaClassifiers;
        this.wekaDataEntityRepository = wekaDataEntityRepository;
    }

    public LearningResponse learning(MultipartFile[] files, String topic, String charset, boolean rebuildClassifier) {
        wekaDataModel = wekaDataModelRepository.findByActiveTrue();
        updateWekaDataModelClasses(topic);

        List<WekaLearningResponseEntity> wekaLearningResponseEntities = analyzeFilesContent(files, charset);
        createWekaDataEntities(topic, wekaLearningResponseEntities);
        Map<String, Integer> dataStatistic = saveResult(wekaLearningResponseEntities, topic);

        if (rebuildClassifier) {
            wekaClassifiers.rebuildClassifiers(wekaDataModel.getModelId());
        }

        return createResponse(wekaLearningResponseEntities, files.length, topic, dataStatistic);
    }

    private void updateWekaDataModelClasses(String topic) {
        List<String> classes = wekaDataModel.getClasses();
        if (!classes.contains(topic)) {
            classes.add(topic);
        }
    }

    private List<WekaLearningResponseEntity> analyzeFilesContent(MultipartFile[] files, String charset) {
        List<WekaLearningResponseEntity> learningResponseEntities = new ArrayList<>();

        for (MultipartFile file : files) {
            Optional<String> fileContent = textReader.getTextContent(file, charset);
            if (!fileContent.isPresent()) break;

            long start = System.currentTimeMillis();
            TextAnalyzer.Response analyzeResponse = textAnalyzer.getTextTerms(fileContent.get());
            long end = System.currentTimeMillis();

            learningResponseEntities.add(new WekaLearningResponseEntity(
                    file.getOriginalFilename(),
                    analyzeResponse.getTextWordsCount(),
                    analyzeResponse.getTextTerms(),
                    (end - start)));
        }

        return learningResponseEntities;
    }

    private void createWekaDataEntities(String topic, List<WekaLearningResponseEntity> wekaLearningResponseEntities) {
        wekaLearningResponseEntities.forEach(wekaLearningResponseEntity -> {
            long start = System.currentTimeMillis();
            double[] data = new double[wekaDataModel.getAttributesNames().size() + 1];
            int countOfSetDataValues = setDataValues(topic, data, wekaLearningResponseEntity.getTextTerms(), wekaLearningResponseEntity.getCountOfWords());
            long end = System.currentTimeMillis();

            wekaLearningResponseEntity.setData(data);
            wekaLearningResponseEntity.setCountOfSetDataValues(countOfSetDataValues);
            wekaLearningResponseEntity.setTime(wekaLearningResponseEntity.getTime() + (end - start));
        });
    }

    private Map<String, Integer> updateDataStatistic(Map<String, Integer> dataStatistic, String topic, int countOfTexts) {
        if (dataStatistic.containsKey(topic)) {
            dataStatistic.put(topic, dataStatistic.get(topic) + countOfTexts);
        } else {
            dataStatistic.put(topic, countOfTexts);
        }
        return dataStatistic;
    }

    private int setDataValues(String topic, double[] data, Map<String, Integer> textTerms, int wordsCount) {
        List<String> attributesNames = wekaDataModel.getAttributesNames();
        int countOfSetValues = 0;
        for (int i = 0; i < attributesNames.size(); i++) {
            if (textTerms.containsKey(attributesNames.get(i))) {
                data[i] = (double) textTerms.get(attributesNames.get(i)) / wordsCount;
                countOfSetValues++;
            } else {
                data[i] = 0;
            }
        }

        data[data.length - 1] = wekaDataModel.getClasses().indexOf(topic);
        return countOfSetValues;
    }

    private Map<String, Integer> saveResult(List<WekaLearningResponseEntity> wekaLearningResponseEntities, String topic) {
        List<WekaDataEntity> wekaDataEntityToSave = wekaLearningResponseEntities.stream()
                .map(wekaLearningResponseEntity ->
                        new WekaDataEntity(wekaDataModel.getModelId(), topic,
                                wekaLearningResponseEntity.getData(),
                                wekaLearningResponseEntity.getCountOfSetDataValues()))
                .collect(Collectors.toList());

        wekaDataEntityRepository.saveAll(wekaDataEntityToSave);

        int countOfWords = wekaLearningResponseEntities.stream()
                .mapToInt(LearningResponseEntity::getCountOfWords)
                .sum();

        Map<String, Integer> dataStatistic = updateDataStatistic(wekaDataModel.getDataStatistic(), topic, countOfWords);
        wekaDataModelRepository.save(
                new WekaDataModel(wekaDataModel.getModelId(), wekaDataModel.getName(), wekaDataModel.getAttributesNames(), wekaDataModel.getClasses(),
                        (wekaDataModel.getSizeOfData() + wekaLearningResponseEntities.size()),
                        (wekaDataModel.getCountOfWords() + countOfWords),
                        dataStatistic, true));

        return dataStatistic;
    }

    private LearningResponse createResponse(List<WekaLearningResponseEntity> wekaLearningResponseEntities,
                                            int inputFilesCount, String topic, Map<String, Integer> dataStatistic) {
        int countOfWords = wekaLearningResponseEntities.stream()
                .mapToInt(LearningResponseEntity::getCountOfWords)
                .sum();

        wekaLearningResponseEntities.forEach(wekaLearningResponseEntity -> wekaLearningResponseEntity
                .setComment("Пустых атрибутов " +
                        wekaLearningResponseEntity.getCountOfSetDataValues() + " из " + wekaDataModel.getAttributesNames().size()));

        return new LearningResponse(topic, inputFilesCount, countOfWords, createComment(dataStatistic), wekaLearningResponseEntities);
    }

    private String createComment(Map<String, Integer> dataStatistic) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Текущая статистика модели ").append(wekaDataModel.getClasses()).append("\n");
        dataStatistic.forEach((key, value) -> stringBuilder.append(key).append(": ").append(value).append("\n"));
        return stringBuilder.toString();
    }
    //    public void buildAttributesFromArrf() {
//        String datasetPath = Objects.requireNonNull(WekaLearningService.class.getClassLoader().getResource("dataset.arff")).getPath();
//
//        Iterable<DictionaryEntity> dictionaryEntities = dictionaryEntityRepository.findAll();
//
//        StringBuilder stringBuilder = new StringBuilder();
//
//        stringBuilder.append("@RELATION text\n\n");
//        dictionaryEntities.forEach(dictionaryEntity ->
//                stringBuilder.append("@ATTRIBUTE ").append(dictionaryEntity.getWord()).append(" numeric\n"));
//        stringBuilder.append("@DATA\n");
//
//        try (FileWriter writer = new FileWriter("dataset.arff")) {
//            writer.write(stringBuilder.toString());
//            writer.flush();
//        } catch (IOException ex) {
//            System.out.println(ex.getMessage());
//        }

//  @Override
//    public LearningResponse learning(MultipartFile[] files, String topic, String charset) {
//        StringBuilder stringBuilder = new StringBuilder();
//        int countOfTexts = 0;
//        int countOfWords = 0;
//        int countOfAttributesContainsTerms = 0;
//
//        for (MultipartFile file : files) {
//            Optional<String> fileContent = textReader.getTextContent(file, charset);
//            if (!fileContent.isPresent()) break;
//
//            TextAnalyzer.Response textTerms = textAnalyzer.getTermsOfText(fileContent.get());
//
//            countOfTexts++;
//            countOfWords += textTerms.getTextWordsCount();
//
//            Object[] result = new Object[attributes.size()];
//            textTerms.getTermsOfText().forEach((term, count) -> {
//                if (attributes.containsKey(term)) {
//                    result[attributes.get(term)] = count;
//                }
//
//                result[result.length - 1] = topic;
//            });
//
//            for (int i = 0; i < result.length; i++) {
//                if (i != result.length - 1) {
//                    if (result[i] != null) {
//                        stringBuilder.append(result[i].toString()).append(", ");
//                    } else {
//                        stringBuilder.append(0).append(", ");
//                    }
//                } else {
//                    stringBuilder.append(result[i].toString());
//                }
//            }
//            stringBuilder.append("\n");
//        }
//
//        try (FileWriter writer = new FileWriter("dataset.arff", true)) {
//            writer.write(stringBuilder.toString());
//            writer.flush();
//        } catch (IOException ex) {
//            System.out.println(ex.getMessage());
//        }
//
//        return new ESLearningResponse();
//    }
//   }
}