package murskiy_sergey.graduate_work.models.text;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.Map;

@Document(indexName = "automatic-text-ranking", type = "texts")
public class TextModel {
    @Id
    private String textName;
    private String textTopic;
    private Map<String, Long> termsOfText;
    private long countOfWords;

    public TextModel(String textName, String textTopic, Map<String, Long> textTerms, long countOfWords) {
        this.textName = textName;
        this.textTopic = textTopic;
        this.termsOfText = textTerms;
        this.countOfWords = countOfWords;
}

    public TextModel() {
    }

    public String getTextName() {
        return textName;
    }

    public void setTextName(String textName) {
        this.textName = textName;
    }

    public String getTextTopic() {
        return textTopic;
    }

    public void setTextTopic(String textTopic) {
        this.textTopic = textTopic;
    }

    public Map<String, Long> getTermsOfText() {
        return termsOfText;
    }

    public void setTermsOfText(Map<String, Long> textTerms) {
        this.termsOfText = textTerms;
    }

    public long getCountOfWords() {
        return countOfWords;
    }

    public void setCountOfWords(long countOfWords) {
        this.countOfWords = countOfWords;
    }
}
