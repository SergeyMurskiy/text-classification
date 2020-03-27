package murskiy_sergey.graduate_work.models.statistic;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.Date;

@Document(indexName = "statistic-index")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Statistic {
    @Id
    @Generated
    private String id;
    private Date date;
    private String methodName;
    private long countOfTexts;
    private long countOfWords;
    private String topic;
    private String status;
    private long time;

    @Builder
    public Statistic(Date date, String methodName, long countOfTexts, long countOfWords, String topic, String status, long time) {
        this.date = date;
        this.methodName = methodName;
        this.countOfTexts = countOfTexts;
        this.countOfWords = countOfWords;
        this.topic = topic;
        this.status = status;
        this.time = time;
    }
}
