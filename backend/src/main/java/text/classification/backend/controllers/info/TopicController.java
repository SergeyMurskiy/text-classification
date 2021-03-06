package text.classification.backend.controllers.info;

import text.classification.backend.models.topic.Topic;
import text.classification.backend.repositories.elasticsearch.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/info/topic")
public class TopicController {
    private final TopicRepository topicRepository;

    @Autowired
    public TopicController(TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }

    @GetMapping(value = "/all")
    public List<Topic> getAllTopics() {
        List<Topic> topicList = new ArrayList<>();
        topicRepository.findAll().forEach(topicList::add);
        return topicList;
    }

    @GetMapping(value = "/delete/all")
    public List<Topic> deleteAllTopics() {
        topicRepository.deleteAll();
        return getAllTopics();
    }
}
