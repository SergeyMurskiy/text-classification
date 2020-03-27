package murskiy_sergey.graduate_work.models.weka.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Document(indexName = "automatic-text-ranking", type = "weka-data-model")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WekaDataModel {
    @Id
    @Generated
    private String modelId;
    private String name;
    private List<String> attributesNames;
    private List<String> classes;
    private long sizeOfData;
    private long countOfWords;
    private Map<String, Long> dataStatistic;
    private boolean active;

    public WekaDataModel(String name, List<String> attributesNames, List<String> classes,
                         long sizeOfData, long countOfWords, Map<String, Long> dataStatistic, boolean active) {
        this.name = name;
        this.attributesNames = attributesNames;
        this.classes = classes;
        this.sizeOfData = sizeOfData;
        this.countOfWords = countOfWords;
        this.dataStatistic = dataStatistic;
        this.active = active;
    }

    public WekaDataModel(String name, List<String> attributesNames, boolean active) {
        this.name = name;
        this.attributesNames = attributesNames;
        classes = new ArrayList<>();
        dataStatistic = new HashMap<>();
        sizeOfData = 0;
        this.active = active;
    }

    public WekaDataModel(String name, List<String> attributesNames, List<String> classes, boolean active) {
        this.name = name;
        this.attributesNames = attributesNames;
        dataStatistic = new HashMap<>();
        this.classes = classes;
        this.active = active;
    }
}
