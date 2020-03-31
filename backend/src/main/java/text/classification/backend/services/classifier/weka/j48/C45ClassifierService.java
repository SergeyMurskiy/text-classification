package text.classification.backend.services.classifier.weka.j48;

import text.classification.common.services.classifier.ClassifierRequest;
import text.classification.common.services.classifier.ClassifierResponseEntity;
import text.classification.backend.services.classifier.weka.WekaClassifierService;
import org.springframework.stereotype.Service;
import weka.classifiers.trees.J48;

@Service
public class C45ClassifierService extends WekaClassifierService {
    @Override
    public String getMethodName() {
        return "C4.5";
    }

    @Override
    protected ClassifierResponseEntity classifierText(ClassifierRequest classifierRequest) throws Exception {
        J48 c4_5Classifier = wekaClassifiers.getJ48Classifier();
        int clsLabel = (int) c4_5Classifier.classifyInstance(createInstance(classifierRequest));

        return ClassifierResponseEntity.builder()
                .textName(classifierRequest.getTextName())
                .wordsCount(classifierRequest.getCountOfWords())
                .classValue(getClassNameByIndex(clsLabel))
                .build();

    }
}
