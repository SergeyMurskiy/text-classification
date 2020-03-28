package murskiy_sergey.graduate_work.services.learning.weka;

import murskiy_sergey.graduate_work.config.WekaClassifiers;
import murskiy_sergey.graduate_work.models.weka.data.WekaDataEntity;
import murskiy_sergey.graduate_work.models.weka.data.WekaDataModel;
import murskiy_sergey.graduate_work.repositories.WekaDataEntityRepository;
import murskiy_sergey.graduate_work.repositories.WekaDataModelRepository;
import murskiy_sergey.graduate_work.services.learning.LearningRequest;
import murskiy_sergey.graduate_work.services.learning.LearningResponseEntity;
import murskiy_sergey.graduate_work.services.learning.LearningServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class WekaLearningService extends LearningServiceImpl {
    private final WekaDataModelRepository wekaDataModelRepository;
    private final WekaClassifiers wekaClassifiers;
    private final WekaDataEntityRepository wekaDataEntityRepository;

    private WekaDataModel wekaDataModel;

    @Autowired
    public WekaLearningService(WekaDataModelRepository wekaDataModelRepository,
                               WekaClassifiers wekaClassifiers,
                               WekaDataEntityRepository wekaDataEntityRepository) {
        this.wekaDataModelRepository = wekaDataModelRepository;
        this.wekaClassifiers = wekaClassifiers;
        this.wekaDataEntityRepository = wekaDataEntityRepository;
    }

    @Override
    public String getMethodName() {
        return "weka";
    }

    @Override
    protected void beforeLearning(String topic) {
        wekaDataModel = wekaDataModelRepository.findByActiveTrue();
        updateWekaDataModelClasses(topic);
    }

    @Override
    protected LearningResponseEntity handleLearningRequest(LearningRequest learningRequest) {
        List<String> attributesNames = wekaDataModel.getAttributesNames();
        Map<String, Long> textTerms = learningRequest.getTextTerms();

        double[] data = new double[wekaDataModel.getAttributesNames().size() + 1];
        int countOfSetValues = 0;
        for (int i = 0; i < attributesNames.size(); i++) {
            if (textTerms.containsKey(attributesNames.get(i))) {
                data[i] = (double) textTerms.get(attributesNames.get(i)) / learningRequest.getCountOfWords();
                countOfSetValues++;
            } else {
                data[i] = 0;
            }
        }

        data[data.length - 1] = wekaDataModel.getClasses().indexOf(learningRequest.getTopic());

        return new WekaLearningResponseEntity(learningRequest.getTextName(), learningRequest.getTopic(), learningRequest.getCountOfWords()
        , data, countOfSetValues);
//        return WekaLearningResponseEntity.builder()
//                .textName(learningRequest.getTextName())
//                .countOfWords(learningRequest.getCountOfWords())
//                .countOfSetDataValues(countOfSetValues)
//                .data(data)
//                .build();
    }

    @Override
    protected <T extends LearningResponseEntity> void saveResult(List<T> learningResponseEntities) {
        List<WekaDataEntity> wekaDataEntityToSave = learningResponseEntities.stream()
                .map(learningRequest -> {
                    WekaLearningResponseEntity wekaLearningResponseEntity = (WekaLearningResponseEntity) learningRequest;
                    return WekaDataEntity.builder()
                            .modelId(wekaDataModel.getModelId())
                            .topicName(wekaLearningResponseEntity.getTopic())
                            .countOfNotEmptyValues(wekaLearningResponseEntity.getCountOfSetDataValues())
                            .data(wekaLearningResponseEntity.getData())
                            .build();
                })
                .collect(Collectors.toList());

        updateDataStatistic(learningResponseEntities.get(0).getTopic(), learningResponseEntities.size(), getTotalWordsCount(learningResponseEntities));
        wekaDataEntityRepository.saveAll(wekaDataEntityToSave);
    }

    @Override
    protected void afterLearning(boolean rebuildClassifier) {
        if (rebuildClassifier) {
            wekaClassifiers.rebuildClassifiers();
        }
    }

    private void updateWekaDataModelClasses(String topic) {
        List<String> classes = wekaDataModel.getClasses();
        if (!classes.contains(topic)) {
            classes.add(topic);
        }
    }

    private Map<String, Long> updateDataStatistic(String topic, long countOfTexts, long countOfWords) {
        wekaDataModel.setSizeOfData(wekaDataModel.getSizeOfData() + countOfTexts);
        wekaDataModel.setCountOfWords(wekaDataModel.getCountOfWords() + countOfWords);

        Map<String, Long> dataStatistic = wekaDataModel.getDataStatistic();

        if (dataStatistic.containsKey(topic)) {
            dataStatistic.put(topic, dataStatistic.get(topic) + countOfTexts);
        } else {
            dataStatistic.put(topic, countOfTexts);
        }
        wekaDataModel.setDataStatistic(dataStatistic);
        wekaDataModelRepository.save(wekaDataModel);

        return dataStatistic;
    }

//    private String createComment(Map<String, Long> dataStatistic) {
//        StringBuilder stringBuilder = new StringBuilder();
//        stringBuilder.append("Текущая статистика модели ").append(wekaDataModel.getClasses()).append("\n");
//        dataStatistic.forEach((key, value) -> stringBuilder.append(key).append(": ").append(value).append("\n"));
//        return stringBuilder.toString();
//    }
}