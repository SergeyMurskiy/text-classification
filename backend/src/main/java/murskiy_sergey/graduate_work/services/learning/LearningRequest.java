package murskiy_sergey.graduate_work.services.learning;

import lombok.Data;
import murskiy_sergey.graduate_work.services.TextAnalyzeResponse;

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
}
