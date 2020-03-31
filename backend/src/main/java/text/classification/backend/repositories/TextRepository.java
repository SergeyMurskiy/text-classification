package text.classification.backend.repositories;

import text.classification.backend.models.text.TextModel;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TextRepository extends ElasticsearchRepository<TextModel, String> {
    List<TextModel> findByTextTopic(String topicName);
}
