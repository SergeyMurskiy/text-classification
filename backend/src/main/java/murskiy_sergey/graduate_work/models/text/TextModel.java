package murskiy_sergey.graduate_work.models.text;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.Map;

@Document(indexName = "automatic-text-ranking", type = "texts")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TextModel {
    @Id
    private String textName;
    private String textTopic;
    private Map<String, Long> termsOfText;
    private long countOfWords;
}
