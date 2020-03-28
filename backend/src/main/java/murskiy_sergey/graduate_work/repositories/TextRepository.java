package murskiy_sergey.graduate_work.repositories;

import murskiy_sergey.graduate_work.models.text.TextModel;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TextRepository extends ElasticsearchRepository<TextModel, String> {
    List<TextModel> findByTextTopic(String topicName);
}
