package murskiy_sergey.graduate_work.models.statistic;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.Date;

@Document(indexName = "statistic-index")
public class Statistic {
    @Id
    private String id;
    private Date date;
    private String methodName;
    private int countOfTexts;
    private int countOfWords;
    private String topic;
    private String status;
    private long time;

    public Statistic(String id, Date date, String methodName, int countOfTexts, int countOfWords, String topic, String status, long time) {
        this.id = id;
        this.date = date;
        this.methodName = methodName;
        this.countOfTexts = countOfTexts;
        this.countOfWords = countOfWords;
        this.topic = topic;
        this.status = status;
        this.time = time;
    }

    public Statistic() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public int getCountOfTexts() {
        return countOfTexts;
    }

    public void setCountOfTexts(int countOfTexts) {
        this.countOfTexts = countOfTexts;
    }

    public int getCountOfWords() {
        return countOfWords;
    }

    public void setCountOfWords(int countOfWords) {
        this.countOfWords = countOfWords;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
