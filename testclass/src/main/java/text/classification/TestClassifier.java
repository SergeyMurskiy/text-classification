package text.classification;

import org.springframework.stereotype.Component;
import text.classification.common.services.classification.ClassifierRequest;
import text.classification.common.services.classification.ClassifierResponseEntity;
import text.classification.common.services.classifier.ClassifierConfig;
import text.classification.common.services.classifier.ClassifierModel;
import text.classification.common.services.learning.LearningRequest;
import text.classification.common.services.learning.LearningResponseEntity;

@Component
public class TestClassifier implements ClassifierModel {
    @Override
    public LearningResponseEntity updateTrainingData(LearningRequest learningRequestList) {
        return null;
    }

    @Override
    public void rebuildClassifier() {

    }

    @Override
    public void buildClassifier(ClassifierConfig classifierConfig) {

    }

    @Override
    public String getClassifierName() {
        return "testtesttest";
    }

    @Override
    public ClassifierResponseEntity classifier(ClassifierRequest classifierRequest) throws Exception {
        return null;
    }
}
