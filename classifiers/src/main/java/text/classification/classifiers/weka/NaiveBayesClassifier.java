package text.classification.classifiers.weka;

import org.springframework.stereotype.Component;
import text.classification.classifiers.weka.WekaClassifier;
import text.classification.common.services.classification.ClassifierRequest;
import text.classification.common.services.classification.ClassifierResponseEntity;
import text.classification.common.services.classifier.ClassifierConfig;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.Instance;

@Component
public class NaiveBayesClassifier extends WekaClassifier {
    private NaiveBayes naiveBayes;

    public NaiveBayesClassifier() {
        this.classifierName = "naive-bayes";
    }

    @Override
    public void buildClassifier(ClassifierConfig classifierConfig) {
        naiveBayes = new NaiveBayes();
        buildInstances(classifierConfig);
        try {
            naiveBayes.buildClassifier(instances);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public ClassifierResponseEntity classifier(ClassifierRequest classifierRequest) throws Exception {
        Instance instance = createInstance(classifierRequest);

        double[] distribution = naiveBayes.distributionForInstance(instance);

        return ClassifierResponseEntity.builder()
                .textName(classifierRequest.getTextName())
                .wordsCount(classifierRequest.getCountOfWords())
                .classValue(getClassNameByDistribution(distribution))
                .comment(createDistributionComment(distribution))
                .build();
    }

    @Override
    public void rebuildClassifier() {
        try {
            naiveBayes.buildClassifier(instances);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String createDistributionComment(double[] distribution) {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < distribution.length; i++) {
            stringBuilder.append(classes.get(i)).append(": ").append((int) (distribution[i] * 100)).append("%\n");
        }

        return stringBuilder.toString();
    }
}
