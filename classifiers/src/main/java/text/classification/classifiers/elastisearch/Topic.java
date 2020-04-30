package text.classification.classifiers.elastisearch;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Topic {
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
