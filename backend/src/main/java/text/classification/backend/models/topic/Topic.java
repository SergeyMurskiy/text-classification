package text.classification.backend.models.topic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "automatic-text-ranking", type = "topic")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Topic {
    @Id
    private String topicName;
    private long textsCount;
    private long totalWordsCount;

    public void addTotalWordsCount(long totalWordsCount) {
        this.totalWordsCount += totalWordsCount;
    }

    public void updateTopicCounts(long documentsCount, long totalWordsCount) {
        this.textsCount += documentsCount;
        this.totalWordsCount += totalWordsCount;
    }
}
