package text.classification.common.services.learning;

import lombok.Data;
import text.classification.common.services.TextAnalyzeResponse;

import java.util.Map;

@Data
public class LearningRequest {
    private String textName;
    private String topic;
    private long countOfWords;
    private Map<String, Long> textTerms;

    public LearningRequest(String textName, String topic, TextAnalyzeResponse textAnalyzeResponse) {
        this.textName = textName;
        this.topic = topic;
        countOfWords = textAnalyzeResponse.getWordsCount();
        textTerms = textAnalyzeResponse.getTextTerms();
    }

    public LearningRequest(String textName, String topic, long countOfWords, Map<String, Long> textTerms) {
        this.textName = textName;
        this.topic = topic;
        this.countOfWords = countOfWords;
        this.textTerms = textTerms;
    }
}
