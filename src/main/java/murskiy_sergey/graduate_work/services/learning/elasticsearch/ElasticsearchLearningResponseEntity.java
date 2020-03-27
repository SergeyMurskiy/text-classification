package murskiy_sergey.graduate_work.services.learning.elasticsearch;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import murskiy_sergey.graduate_work.services.learning.LearningResponseEntity;

@EqualsAndHashCode(callSuper = true)
@Data
public class ElasticsearchLearningResponseEntity extends LearningResponseEntity {
    public ElasticsearchLearningResponseEntity(String textName, String topic, long countOfWords, long time, String comment) {
        super(textName, topic, countOfWords, time, comment);
    }
    @Builder
    public ElasticsearchLearningResponseEntity(String textName, String topic, long countOfWords) {
        super(textName, topic, countOfWords);
    }
}
