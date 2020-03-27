package murskiy_sergey.graduate_work.config;

import murskiy_sergey.graduate_work.models.weka.data.WekaDataEntity;
import murskiy_sergey.graduate_work.models.weka.data.WekaDataModel;
import murskiy_sergey.graduate_work.repositories.WekaDataEntityRepository;
import murskiy_sergey.graduate_work.repositories.WekaDataModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.trees.J48;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;

import java.util.ArrayList;
import java.util.List;

@Component
public class WekaClassifiers {
    private final WekaDataEntityRepository wekaDataEntityRepository;
    private final WekaDataModelRepository wekaDataModelRepository;

    private String relationName;

    private List<String> attributesNames;
    private ArrayList<Attribute> attributes;
    private List<String> classes;
    private Instances instances;
    private boolean isBuild;

    private J48 J48Classifier;
    private NaiveBayes naiveBayesClassifier;

    @Autowired
    WekaClassifiers(WekaDataEntityRepository wekaDataEntityRepository, WekaDataModelRepository wekaDataModelRepository) {
        this.wekaDataEntityRepository = wekaDataEntityRepository;
        this.wekaDataModelRepository = wekaDataModelRepository;

       // isBuild = buildInstances(this.wekaDataModelRepository.findByActiveTrue());
        if (isBuild) {
            buildJ48Classifier();
            buildNaiveBayesClassifier();
        }
    }

    public boolean rebuildClassifiers() {
        isBuild = buildInstances(wekaDataModelRepository.findByActiveTrue());
        if (isBuild) {
            buildJ48Classifier();
            buildNaiveBayesClassifier();
        }
        return isBuild;
    }

    private void buildJ48Classifier() {
        J48Classifier = new J48();

        String[] options = new String[1];
        options[0] = "-U";

        try {
            J48Classifier.setOptions(options);
            J48Classifier.buildClassifier(instances);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void buildNaiveBayesClassifier() {
        naiveBayesClassifier = new NaiveBayes();

        try {
            naiveBayesClassifier.buildClassifier(instances);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean buildInstances(WekaDataModel wekaDataModel) {
        if (wekaDataModel != null && wekaDataModel.getClasses().size() > 1) {
            relationName = wekaDataModel.getName();
            ArrayList<Attribute> attributes = buildAttributes(wekaDataModel);

            instances = new Instances(relationName, attributes, 0);
            instances.setClassIndex(instances.numAttributes() - 1);

            List<WekaDataEntity> dataEntityList = wekaDataEntityRepository.findByModelId(wekaDataModel.getModelId());
            dataEntityList.forEach(dataEntity -> instances.add(new DenseInstance(1, dataEntity.getData())));

            return true;
        }
        return false;
    }

    private ArrayList<Attribute> buildAttributes(WekaDataModel wekaDataEntity) {
        attributesNames = wekaDataEntity.getAttributesNames();
        attributes = new ArrayList<>();

        for (String attr : attributesNames) {
            attributes.add(new Attribute(attr));
        }

        classes = wekaDataEntity.getClasses();
        attributes.add(new Attribute("class", classes));

        return attributes;
    }

    public J48 getJ48Classifier() {
        return J48Classifier;
    }

    public NaiveBayes getNaiveBayesClassifier() {
        return naiveBayesClassifier;
    }

    public String getRelationName() {
        return relationName;
    }

    public ArrayList<Attribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(ArrayList<Attribute> attributes) {
        this.attributes = attributes;
    }

    public List<String> getClasses() {
        return classes;
    }

    public List<String> getAttributesNames() {
        return attributesNames;
    }

    public boolean isBuild() {
        return isBuild;
    }

    public void setBuild(boolean build) {
        isBuild = build;
    }
}
