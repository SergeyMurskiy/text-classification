package murskiy_sergey.graduate_work.services.learning.weka;

import lombok.*;
import murskiy_sergey.graduate_work.services.learning.LearningResponseEntity;


@EqualsAndHashCode(callSuper = true)
@Data
public class WekaLearningResponseEntity extends LearningResponseEntity {
    private double[] data;
    private long countOfSetDataValues;

    public WekaLearningResponseEntity(String textName, String topic, long countOfWords, long time, String comment, double[] data, long countOfSetDataValues) {
        super(textName, topic, countOfWords, time, comment);
        this.data = data;
        this.countOfSetDataValues = countOfSetDataValues;
    }

    @Builder
    public WekaLearningResponseEntity(String textName, String topic, long countOfWords, double[] data, long countOfSetDataValues) {
        super(textName, topic, countOfWords);
        this.data = data;
        this.countOfSetDataValues = countOfSetDataValues;
    }
}
