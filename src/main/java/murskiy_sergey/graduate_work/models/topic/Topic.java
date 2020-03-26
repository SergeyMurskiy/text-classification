package murskiy_sergey.graduate_work.models.topic;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "automatic-text-ranking", type = "topic")
public class Topic {
    @Id
    private String topicName;
    private long textsCount;
    private long totalWordsCount;

    public Topic(String topicName, long textsCount, long totalWordsCount) {
        this.topicName = topicName;
        this.textsCount = textsCount;
        this.totalWordsCount = totalWordsCount;
    }

    public Topic() {}

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public long getTextsCount() {
        return textsCount;
    }

    public void setTextsCount(long textsCount) {
        this.textsCount = textsCount;
    }

    public long getTotalWordsCount() {
        return totalWordsCount;
    }

    public void setTotalWordsCount(long totalWordsCount) {
        this.totalWordsCount = totalWordsCount;
    }

    public void addTotalWordsCount(long totalWordsCount) {
        this.totalWordsCount += totalWordsCount;
    }

    public void updateTopicCounts(long documentsCount, long totalWordsCount) {
        this.textsCount += documentsCount;
        this.totalWordsCount += totalWordsCount;
    }
}
