package murskiy_sergey.graduate_work.models.term;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.HashMap;
import java.util.Map;

@Document(indexName = "automatic-text-ranking", type = "term")
public class Term {
    @Id
    private String name;
    private long countOfTexts;
    private Map<String, Long> topicsInfo;

    public Term(String name, long countOfTexts, Map<String, Long> topicsInfo) {
        this.name = name;
        this.countOfTexts = countOfTexts;
        this.topicsInfo = topicsInfo;
    }

    public Term(String name, String topicName, long termCount) {
        this.name = name;
        this.countOfTexts = 1;
        topicsInfo = new HashMap<>();
        topicsInfo.put(topicName, termCount);
    }

    public Term() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCountOfTextContainsTerm() {
        return countOfTexts;
    }

    public void setCountOfTextContainsTerm(long countOfTextContainsTerm) {
        this.countOfTexts = countOfTextContainsTerm;
    }

    public Map<String, Long> getTopicsInfo() {
        return topicsInfo;
    }

    public void setTopicsInfo(Map<String, Long> topicsInfo) {
        this.topicsInfo = topicsInfo;
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
