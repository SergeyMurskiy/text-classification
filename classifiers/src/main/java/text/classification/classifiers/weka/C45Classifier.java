package text.classification.classifiers.weka;

import org.springframework.stereotype.Component;
import text.classification.common.services.classification.ClassifierRequest;
import text.classification.common.services.classification.ClassifierResponseEntity;
import text.classification.common.services.classifier.ClassifierConfig;
import weka.classifiers.trees.J48;

@Component
public class C45Classifier extends WekaClassifier {
    private J48 j48Classifier;

    public C45Classifier() {
        this.classifierName = "C4.5";
    }

    @Override
    public void buildClassifier(ClassifierConfig classifierConfig) {
        buildInstances(classifierConfig);
        j48Classifier = new J48();

        String[] options = new String[1];
        options[0] = "-U";

        try {
            j48Classifier.setOptions(options);
            j48Classifier.buildClassifier(instances);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public ClassifierResponseEntity classifier(ClassifierRequest classifierRequest) throws Exception {
        int clsLabel = (int) j48Classifier.classifyInstance(createInstance(classifierRequest));

        return ClassifierResponseEntity.builder()
                .textName(classifierRequest.getTextName())
                .wordsCount(classifierRequest.getCountOfWords())
                .classValue(getClassNameByIndex(clsLabel))
                .build();
    }

    @Override
    public void rebuildClassifier() {
        try {
            this.j48Classifier.buildClassifier(instances);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
