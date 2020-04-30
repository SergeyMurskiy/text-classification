package text.classification.backend.services.statistic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import text.classification.common.services.classifier.ClassifierModel;
import text.classification.common.services.learning.LearningRequest;
import text.classification.common.services.learning.LearningResponse;
import text.classification.common.services.learning.LearningResponseEntity;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LearningService {
    private final List<ClassifierModel> classifiers;

    @Autowired
    public LearningService(List<ClassifierModel> classifiers) {
        this.classifiers = classifiers;
    }

    public List<LearningResponse> learning(List<LearningRequest> learningRequests) {
        return classifiers.stream()
                .map(classifier -> learningProcess(classifier, learningRequests, learningRequests.get(0).getTopic(), learningRequests.size()))
                .collect(Collectors.toList());
    }

    public void rebuildClassifiers() {
        classifiers.forEach(ClassifierModel::rebuildClassifier);
    }

    private LearningResponse learningProcess(ClassifierModel classifier, List<LearningRequest> learningRequestList, String topic, long countOfInputTexts) {
        long startLearningProcess = System.currentTimeMillis();

        List<LearningResponseEntity> learningResponseEntities = learningRequestList.stream()
                .map(learningRequest -> {
                    long start = System.currentTimeMillis();
                    LearningResponseEntity learningResponseEntity = classifier.updateTrainingData(learningRequest);
                    long end = System.currentTimeMillis();
                    learningResponseEntity.setTime(end - start);
                    return learningResponseEntity;
                })
                .collect(Collectors.toList());

        long endLearningProcess = System.currentTimeMillis();
        return createResponse(classifier.getClassifierName(), learningResponseEntities,
                countOfInputTexts, topic, (endLearningProcess - startLearningProcess));
    }

    private <T extends LearningResponseEntity> LearningResponse createResponse(String classifierName, List<T> learningResponseEntities, long inputFilesCount,
                                                                               String topic, long time) {
        return LearningResponse.builder()
                .topic(topic)
                .classifierName(classifierName)
                .countOfTexts(inputFilesCount)
                .countOfWords(getTotalWordsCount(learningResponseEntities))
                .responseEntities(learningResponseEntities)
                .time(time)
                .build();
    }

    private  <T extends LearningResponseEntity> long getTotalWordsCount(List<T> learningResponseEntityList) {
        return learningResponseEntityList.stream()
                .mapToLong(LearningResponseEntity::getCountOfWords)
                .sum();
    }
}
