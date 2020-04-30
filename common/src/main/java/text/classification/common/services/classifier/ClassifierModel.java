package text.classification.common.services.classifier;

import text.classification.common.services.classification.ClassifierRequest;
import text.classification.common.services.classification.ClassifierResponseEntity;
import text.classification.common.services.learning.LearningRequest;
import text.classification.common.services.learning.LearningResponseEntity;


    public interface ClassifierModel {

        LearningResponseEntity updateTrainingData(LearningRequest learningRequestList);

        void rebuildClassifier();

        void buildClassifier(ClassifierConfig classifierConfig);

        String getClassifierName();

        ClassifierResponseEntity classifier(ClassifierRequest classifierRequest) throws Exception;
    }

