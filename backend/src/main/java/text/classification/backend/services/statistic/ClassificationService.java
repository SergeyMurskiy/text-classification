package text.classification.backend.services.statistic;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import text.classification.common.services.classification.ClassifierRequest;
import text.classification.common.services.classification.ClassifierResponse;
import text.classification.common.services.classification.ClassifierResponseEntity;
import text.classification.common.services.classifier.ClassifierModel;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ClassificationService {
    private final List<ClassifierModel> classifiers;

    @Autowired
    public ClassificationService(List<ClassifierModel> classifiers) {
        this.classifiers = classifiers;
    }

    public List<ClassifierResponse> classifier(List<ClassifierRequest> classifierRequests) {
        return classifiers.stream()
                .map(classifier -> {
                    long start = System.currentTimeMillis();
                    List<ClassifierResponseEntity> responseEntities = classifierRequests.stream()
                            .map(classifierRequest -> classifierProcess(classifier, classifierRequest))
                            .collect(Collectors.toList());
                    long end = System.currentTimeMillis();

                    return ClassifierResponse.builder()
                            .responseEntities(responseEntities)
                            .classifierName(classifier.getClassifierName())
                            .textCount(classifierRequests.size())
                            .time(end - start)
                            .build();
                }).collect(Collectors.toList());
    }

    private ClassifierResponseEntity classifierProcess(ClassifierModel classifier, ClassifierRequest classifierRequest) {
        try {
            long methodStart = System.currentTimeMillis();
            ClassifierResponseEntity classifierResponseEntity = classifier.classifier(classifierRequest);
            long methodEnd = System.currentTimeMillis();
            classifierResponseEntity.setTime(methodEnd - methodStart);
            return classifierResponseEntity;
        } catch (Exception e) {
            return createErrorResponse(classifier, classifierRequest);
        }
    }

    private ClassifierResponseEntity createErrorResponse(ClassifierModel classifier, ClassifierRequest classifierRequest) {
        log.error("Can't classification text with name: " + classifierRequest.getTextName() + " by method " + classifier.getClassifierName());
        return ClassifierResponseEntity.builder()
                .textName(classifierRequest.getTextName())
                .wordsCount(classifierRequest.getCountOfWords())
                .comment("Во время классификации текста произошла ошибка")
                .build();
    }
}
