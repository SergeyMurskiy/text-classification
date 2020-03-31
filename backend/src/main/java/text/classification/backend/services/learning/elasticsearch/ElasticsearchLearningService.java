package text.classification.backend.services.learning.elasticsearch;

import text.classification.backend.models.term.Term;
import text.classification.backend.models.topic.Topic;
import text.classification.backend.repositories.elasticsearch.TermRepository;
import text.classification.backend.repositories.elasticsearch.TopicRepository;
import text.classification.common.services.learning.LearningRequest;
import text.classification.common.services.learning.LearningResponseEntity;
import text.classification.common.services.learning.LearningServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ElasticsearchLearningService extends LearningServiceImpl {
    private final TermRepository termRepository;
    private final TopicRepository topicRepository;

    private Map<String, Term> termsToSave;
    @Autowired
    public ElasticsearchLearningService(TermRepository termRepository, TopicRepository topicRepository) {
        this.termRepository = termRepository;
        this.topicRepository = topicRepository;
    }

    @Override
    public String getMethodName() {
        return "elasticsearch";
    }

    @Override
    protected void beforeLearning(String topic) {
        termsToSave = new HashMap<>();
    }

    @Override
    protected LearningResponseEntity handleLearningRequest(LearningRequest learningRequest) {
        Map<String, Long> textTerms = learningRequest.getTextTerms();
        textTerms.forEach((term, count) -> {
            Term savedTerm = termsToSave.get(term);

            if (savedTerm == null) {
                Optional<Term> savedTermOP = termRepository.findById(term);
                if (savedTermOP.isPresent()) {
                    savedTerm = savedTermOP.get();
                }
            }

            if (savedTerm == null) {
                Term termToSave = new Term(term, learningRequest.getTopic(), count);
                termsToSave.put(term, termToSave);
            } else {
                savedTerm.incrementCountOfTexts();
                savedTerm.incrementTopicInfoTermCount(learningRequest.getTopic(), count);
                termsToSave.put(term, savedTerm);
            }
        });
        return new ElasticsearchLearningResponseEntity(learningRequest.getTextName(), learningRequest.getTopic(),
                learningRequest.getCountOfWords());
//        return ElasticsearchLearningResponseEntity.builder()
//                .textName(learningRequest.getTextName())
//                .countOfWords(learningRequest.getCountOfWords())
//                .topic(learningRequest.getTopic())
//                .build();
    }

    @Override
    protected <T extends LearningResponseEntity> void saveResult(List<T> learningResponseEntity) {
        termRepository.saveAll(termsToSave.values());

        String topic = learningResponseEntity.get(0).getTopic();
        Optional<Topic> savedTopicOP = topicRepository.findById(topic);

        if (!savedTopicOP.isPresent()) {
            topicRepository.save(new Topic(topic, learningResponseEntity.size(), getTotalWordsCount(learningResponseEntity)));
        } else {
            Topic savedTopic = savedTopicOP.get();
            savedTopic.updateTopicCounts(learningResponseEntity.size(), getTotalWordsCount(learningResponseEntity));
            topicRepository.save(savedTopic);
        }
    }

    @Override
    protected void afterLearning(boolean rebuildClassifier) {

    }
}
