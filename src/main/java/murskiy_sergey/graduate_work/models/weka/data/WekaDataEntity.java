package murskiy_sergey.graduate_work.models.weka.data;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;


@Document(indexName = "automatic-text-ranking", type = "weka-data-entity")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WekaDataEntity {
    @Id
    @Generated
    private String id;
    private String modelId;
    private String topicName;
    private long countOfNotEmptyValues;
    private double[] data;
}
