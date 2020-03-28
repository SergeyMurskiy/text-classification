package murskiy_sergey.graduate_work.services.learning;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LearningResponseEntity {
    private String textName;
    private String topic;
    private long countOfWords;
    private long time;
    private String comment;

    public LearningResponseEntity(String textName, String topic, long countOfWords, long time, String comment) {
        this.textName = textName;
        this.topic = topic;
        this.countOfWords = countOfWords;
        this.time = time;
        this.comment = comment;
    }

    public LearningResponseEntity(String textName, String topic, long countOfWords) {
        this.textName = textName;
        this.topic = topic;
        this.countOfWords = countOfWords;
    }
}
