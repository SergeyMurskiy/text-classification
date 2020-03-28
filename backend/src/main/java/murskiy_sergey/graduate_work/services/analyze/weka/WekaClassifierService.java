package murskiy_sergey.graduate_work.services.analyze.weka;

import murskiy_sergey.graduate_work.config.WekaClassifiers;
import murskiy_sergey.graduate_work.services.analyze.ClassifierRequest;
import murskiy_sergey.graduate_work.services.analyze.ClassifierServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

import java.util.ArrayList;
import java.util.Map;

public abstract class WekaClassifierService extends ClassifierServiceImpl {
    private Instances dataset;

    @Autowired
    protected WekaClassifiers wekaClassifiers;

    @Override
    protected void beforeClassifier() {
        dataset = createDataset();
    }

    @Override
    protected void afterClassifier() {
    }

    protected Instance createInstance(ClassifierRequest classifierRequest) {
        ArrayList<Attribute> attributes = wekaClassifiers.getAttributes();
        Instance instance = new DenseInstance(attributes.size());

        Map<String, Long> textTerms = classifierRequest.getTextTerms();
        long wordsCount = classifierRequest.getCountOfWords();

        attributes.forEach(attribute -> {
            double attrVal = (double) textTerms.getOrDefault(attribute.name(), 0L) / wordsCount;
            instance.setValue(attribute, attrVal);
        });

        instance.setDataset(dataset);
        return instance;
    }

    protected String getClassNameByDistribution(double[] distribution) {
        return wekaClassifiers.getClasses().get(getMaxIndexInDistribution(distribution));
    }

    protected String getClassNameByIndex(int index) {
        return wekaClassifiers.getClasses().get(index);
    }

    private Instances createDataset() {
        Instances dataset = new Instances(wekaClassifiers.getRelationName(), wekaClassifiers.getAttributes(), 1);
        dataset.setClassIndex(dataset.numAttributes() - 1);
        return dataset;
    }

    private int getMaxIndexInDistribution(double[] distribution) {
        double max = 0;
        int index = 0;

        for (int i = 0; i < distribution.length; i++) {
            if (max < distribution[i]) {
                max = distribution[i];
                index = i;
            }
        }

        return index;
    }
}
