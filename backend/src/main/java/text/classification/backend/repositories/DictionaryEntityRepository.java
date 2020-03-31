package text.classification.backend.repositories;

import text.classification.backend.models.dictionary.DictionaryEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface DictionaryEntityRepository extends ElasticsearchRepository<DictionaryEntity, String> {
    List<DictionaryEntity> findByWord(String word);
}
