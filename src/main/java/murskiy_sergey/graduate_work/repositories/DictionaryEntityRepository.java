package murskiy_sergey.graduate_work.repositories;

import murskiy_sergey.graduate_work.models.dictionary.DictionaryEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface DictionaryEntityRepository extends ElasticsearchRepository<DictionaryEntity, String> {
    List<DictionaryEntity> findByWord(String word);
}
