package murskiy_sergey.graduate_work.models.weka.data;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Document(indexName = "automatic-text-ranking", type = "weka-data-model")
public class WekaDataModel {
    @Id
    private String modelId;
    private String name;
    private List<String> attributesNames;
    private List<String> classes;
    private int sizeOfData;
    private int countOfWords;
    private Map<String, Integer> dataStatistic;
    private boolean active;

    public WekaDataModel(String modelId, String name, List<String> attributesNames, List<String> classes,
                         int sizeOfData, int countOfWords, Map<String, Integer> dataStatistic, boolean active) {
        this.modelId = modelId;
        this.name = name;
        this.attributesNames = attributesNames;
        this.classes = classes;
        this.sizeOfData = sizeOfData;
        this.countOfWords = countOfWords;
        this.dataStatistic = dataStatistic;
        this.active = active;
    }

    public WekaDataModel(String modelId, String name, List<String> attributesNames, boolean active) {
        this.modelId = modelId;
        this.name = name;
        this.attributesNames = attributesNames;
        classes = new ArrayList<>();
        dataStatistic = new HashMap<>();
        sizeOfData = 0;
        this.active = active;
    }

    public WekaDataModel(String modelId, String name, List<String> attributesNames, List<String> classes, boolean active) {
        this.modelId = modelId;
        this.name = name;
        this.attributesNames = attributesNames;
        dataStatistic = new HashMap<>();
        this.classes = classes;
        this.active = active;
    }

    public WekaDataModel() {
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getAttributesNames() {
        return attributesNames;
    }

    public void setAttributesNames(List<String> attributesNames) {
        this.attributesNames = attributesNames;
    }

    public List<String> getClasses() {
        return classes;
    }

    public void setClasses(List<String> classes) {
        this.classes = classes;
    }

    public int getSizeOfData() {
        return sizeOfData;
    }

    public void setSizeOfData(int sizeOfData) {
        this.sizeOfData = sizeOfData;
    }

    public int getCountOfWords() {
        return countOfWords;
    }

    public void setCountOfWords(int countOfWords) {
        this.countOfWords = countOfWords;
    }

    public Map<String, Integer> getDataStatistic() {
        return dataStatistic;
    }

    public void setDataStatistic(Map<String, Integer> dataStatistic) {
        this.dataStatistic = dataStatistic;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
