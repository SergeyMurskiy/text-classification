package text.classification.common.services.classification;

import lombok.AllArgsConstructor;
import lombok.Data;
import text.classification.common.services.TextAnalyzeResponse;

import java.util.Map;

@Data
@AllArgsConstructor
public class ClassifierRequest {
    private String textName;
    private long countOfWords;
    private Map<String, Long> textTerms;

    public ClassifierRequest(String textName, TextAnalyzeResponse textAnalyzeResponse) {
        this.textName = textName;
        countOfWords = textAnalyzeResponse.getWordsCount();
        textTerms = textAnalyzeResponse.getTextTerms();
    }
}
