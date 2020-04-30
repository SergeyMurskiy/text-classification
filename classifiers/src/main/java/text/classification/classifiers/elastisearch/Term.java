package text.classification.classifiers.elastisearch;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Term {
    private String name;
    private long countOfTexts;
    private Map<String, Long> topicsInfo;

    public Term(String name, String topicName, long termCount) {
        this.name = name;
        this.countOfTexts = 1;
        topicsInfo = new HashMap<>();
        topicsInfo.put(topicName, termCount);
    }

    public boolean containsTopic(String topicName) {
        return topicsInfo.containsKey(topicName);
    }

    public void incrementTopicInfoTermCount(String topicName, long termCount) {
        long oldTermCount = topicsInfo.containsKey(topicName) ? topicsInfo.get(topicName) : 0;
        topicsInfo.put(topicName, oldTermCount + termCount);
    }

    public void incrementCountOfTexts() {
        countOfTexts++;
    }

}
