package text.classification.backend.repositories.elasticsearch;

import text.classification.backend.models.topic.Topic;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface TopicRepository extends ElasticsearchRepository<Topic, String> {
    Topic findByTopicName(String topicName);
    void deleteByTopicName(String topicName);
}
