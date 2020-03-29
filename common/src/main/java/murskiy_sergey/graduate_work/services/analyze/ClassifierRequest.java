package murskiy_sergey.graduate_work.services.analyze;

import lombok.AllArgsConstructor;
import lombok.Data;
import murskiy_sergey.graduate_work.services.TextAnalyzeResponse;

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
