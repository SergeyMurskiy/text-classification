package murskiy_sergey.graduate_work.models.dictionary;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "automatic-text-ranking", type = "dictionary")
public class DictionaryEntity {
    @Id
    private String word;

    public DictionaryEntity(String word) {
        this.word = word;
    }

    public DictionaryEntity(){}

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }
}
