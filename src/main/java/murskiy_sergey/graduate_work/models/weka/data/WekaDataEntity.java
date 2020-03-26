package murskiy_sergey.graduate_work.models.weka.data;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.UUID;


@Document(indexName = "automatic-text-ranking", type = "weka-data-entity")
public class WekaDataEntity {
    @Id
    private String id;
    private String modelId;
    private String topicName;
    private int countOfNotEmptyValues;
    private double[] data;

    public WekaDataEntity(String modelId, String topicName, double[] data, int countOfNotEmptyValues) {
        id = UUID.randomUUID().toString();
        this.modelId = modelId;
        this.topicName = topicName;
        this.data = data;
        this.countOfNotEmptyValues = countOfNotEmptyValues;
    }

    public WekaDataEntity(String id, String modelId, String topicName, double[] data) {
        this.id = id;
        this.modelId = modelId;
        this.topicName = topicName;
        this.data = data;
    }

    public WekaDataEntity() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public double[] getData() {
        return data;
    }

    public void setData(double[] data) {
        this.data = data;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public int getCountOfNotEmptyValues() {
        return countOfNotEmptyValues;
    }

    public void setCountOfNotEmptyValues(int countOfNotEmptyValues) {
        this.countOfNotEmptyValues = countOfNotEmptyValues;
    }
}
