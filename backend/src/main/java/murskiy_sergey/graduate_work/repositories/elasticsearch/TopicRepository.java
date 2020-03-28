package murskiy_sergey.graduate_work.repositories.elasticsearch;

import murskiy_sergey.graduate_work.models.topic.Topic;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface TopicRepository extends ElasticsearchRepository<Topic, String> {
    Topic findByTopicName(String topicName);
    void deleteByTopicName(String topicName);
}
