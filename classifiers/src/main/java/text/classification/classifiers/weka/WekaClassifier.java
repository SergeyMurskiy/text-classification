package text.classification.classifiers.weka;

import text.classification.common.services.classification.ClassifierRequest;
import text.classification.common.services.classification.ClassifierResponseEntity;
import text.classification.common.services.classifier.ClassifierConfig;
import text.classification.common.services.classifier.ClassifierModel;
import text.classification.common.services.learning.LearningRequest;
import text.classification.common.services.learning.LearningResponseEntity;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class WekaClassifier implements ClassifierModel {
    protected List<String> attributesNames;
    protected Instances instances;
    protected ArrayList<Attribute> attributes;
    protected List<String> classes;
    protected String classifierName;

    protected void buildInstances(ClassifierConfig classifierConfig) {
        attributesNames = classifierConfig.getAttributeNames();
        ArrayList<Attribute> attributes = buildAttributes(classifierConfig);
        instances = new Instances(classifierName, attributes, 0);
        instances.setClassIndex(instances.numAttributes() - 1);
    }

    private ArrayList<Attribute> buildAttributes(ClassifierConfig classifierConfig) {
        attributes = new ArrayList<>();

        for (String attr : attributesNames) {
            attributes.add(new Attribute(attr));
        }

        classes = classifierConfig.getTextClasses();
        attributes.add(new Attribute("class", classes));

        return attributes;
    }

    protected Instance createInstance(ClassifierRequest classifierRequest) {
        Instance instance = new DenseInstance(attributes.size());

        Map<String, Long> textTerms = classifierRequest.getTextTerms();
        long wordsCount = classifierRequest.getCountOfWords();

        attributes.forEach(attribute -> {
            double attrVal = (double) textTerms.getOrDefault(attribute.name(), 0L) / wordsCount;
            instance.setValue(attribute, attrVal);
        });

        instance.setDataset(createDataset());
        return instance;
    }

    protected String getClassNameByDistribution(double[] distribution) {
        return classes.get(getMaxIndexInDistribution(distribution));
    }

    protected String getClassNameByIndex(int index) {
        return classes.get(index);
    }

    private Instances createDataset() {
        Instances dataset = new Instances(classifierName, attributes, 1);
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

    @Override
    public LearningResponseEntity updateTrainingData(LearningRequest learningRequest) {
        Map<String, Long> textTerms = learningRequest.getTextTerms();

        double[] data = new double[attributesNames.size() + 1];
        int countOfSetValues = 0;

        for (int i = 0; i < attributesNames.size(); i++) {
            if (textTerms.containsKey(attributesNames.get(i))) {
                data[i] = (double) textTerms.get(attributesNames.get(i)) / learningRequest.getCountOfWords();
                countOfSetValues++;
            } else {
                data[i] = 0;
            }
        }

        data[data.length - 1] = classes.indexOf(learningRequest.getTopic());
        this.instances.add(new DenseInstance(1, data));

        return WekaLearningResponseEntity.builder()
                .textName(learningRequest.getTextName())
                .countOfWords(learningRequest.getCountOfWords())
                .countOfSetDataValues(countOfSetValues)
                .data(data)
                .build();
    }

    public abstract ClassifierResponseEntity classifier(ClassifierRequest classifierRequest) throws Exception;

    @Override
    public String getClassifierName() {
        return classifierName;
    }
}
