package text.classification.backend.models.dictionary;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "automatic-text-ranking", type = "dictionary")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DictionaryEntity {
    @Id
    private String word;
}