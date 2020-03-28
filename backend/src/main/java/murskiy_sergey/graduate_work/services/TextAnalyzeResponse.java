package murskiy_sergey.graduate_work.services;

import lombok.Data;

import java.util.Map;

@Data
public class TextAnalyzeResponse {
    private String textName;
    private long wordsCount;
    private Map<String, Long> textTerms;

    public TextAnalyzeResponse(long wordsCount, Map<String, Long> textTerms) {
        this.wordsCount = wordsCount;
        this.textTerms = textTerms;
    }

}