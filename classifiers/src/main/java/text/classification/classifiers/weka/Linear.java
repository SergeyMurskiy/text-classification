package text.classification.classifiers.weka;

import org.springframework.stereotype.Component;
import text.classification.common.services.classification.ClassifierRequest;
import text.classification.common.services.classification.ClassifierResponseEntity;
import text.classification.common.services.classifier.ClassifierConfig;
import weka.classifiers.Classifier;
import weka.classifiers.functions.SGDText;

@Component
public class Linear extends WekaClassifier {
    private SGDText sgdText;

    @Override
    public void rebuildClassifier() {

    }

    public Linear() {
        this.classifierName = "testweka";
    }

    @Override
    public void buildClassifier(ClassifierConfig classifierConfig) {
        buildInstances(classifierConfig);

        SGDText sgdText = new SGDText();
        try {
            sgdText.buildClassifier(instances);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public ClassifierResponseEntity classifier(ClassifierRequest classifierRequest) throws Exception {
        double[] distribution = sgdText.distributionForInstance(createInstance(classifierRequest));

        return ClassifierResponseEntity.builder()
                .textName(classifierRequest.getTextName())
                .wordsCount(classifierRequest.getCountOfWords())
                .classValue(getClassNameByDistribution(distribution))
                .comment(createDistributionComment(distribution))
                .build();
    }

    private String createDistributionComment(double[] distribution) {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < distribution.length; i++) {
            stringBuilder.append(classes.get(i)).append(": ").append((int) (distribution[i] * 100)).append("%\n");
        }

        return stringBuilder.toString();
    }
}
