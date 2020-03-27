package murskiy_sergey.graduate_work.services.analyze.elasticsearch;

import murskiy_sergey.graduate_work.models.term.Term;
import murskiy_sergey.graduate_work.models.topic.Topic;
import murskiy_sergey.graduate_work.repositories.elasticsearch.TermRepository;
import murskiy_sergey.graduate_work.repositories.elasticsearch.TopicRepository;
import murskiy_sergey.graduate_work.services.analyze.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ElasticsearchClassifierService extends ClassifierServiceImpl {
    private TermRepository termRepository;
    private TopicRepository topicRepository;

    @Autowired
    public ElasticsearchClassifierService(TermRepository termRepository, TopicRepository topicRepository) {
        this.termRepository = termRepository;
        this.topicRepository = topicRepository;
    }

    @Override
    public String getMethodName() {
        return "elasticsearch";
    }

    @Override
    protected void beforeClassifier() {
    }

    @Override
    protected ClassifierResponseEntity classifierText(ClassifierRequest classifierRequest) throws Exception{
        Map<String, Double> result = new HashMap<>();
        long countOfWordsInText = classifierRequest.getCountOfWords();

        List<Topic> topicList = new ArrayList<>();
        topicRepository.findAll().forEach(topicList::add);

        long totalDocumentCount = topicList.stream()
                .mapToLong(Topic::getTextsCount)
                .sum();

        Map<String, Long> topics = getMapOfTopic(topicList);

        classifierRequest.getTextTerms().forEach((term, count) -> {
            Optional<Term> savedTerms = termRepository.findById(term);

            if (savedTerms.isPresent()) {
                Term savedTerm = savedTerms.get();
                savedTerm.getTopicsInfo().forEach((topicName, wordsCount) -> {
                    double w = Math.abs((double) wordsCount / topics.get(topicName) -
                            (double) count / countOfWordsInText) *
                            1.0 / (1 + Math.log((double) totalDocumentCount / savedTerm.getCountOfTexts()));
                    if (result.containsKey(topicName)) {
                        double oldValue = result.get(topicName);
                        result.put(topicName, oldValue + w);
                    } else {
                        result.put(topicName, w);
                    }
                });
            }
        });

        Optional<Map.Entry<String, Double>> entry = result.entrySet().stream().min(Comparator.comparingDouble(Map.Entry::getValue));

        if (entry.isPresent()) {
            return ClassifierResponseEntity.builder()
                    .textName(classifierRequest.getTextName())
                    .wordsCount(classifierRequest.getCountOfWords())
                    .classValue(entry.get().getKey())
                    .comment(createComment(result))
                    .build();
        } else {
            throw new Exception();
        }
    }

    @Override
    protected void afterClassifier() {
    }

    private String createComment(Map<String, Double> result) {
        StringBuilder stringBuilder = new StringBuilder();
        result.forEach((key, value) -> stringBuilder.append(key).append(" : ").append(value).append("\n"));
        return stringBuilder.toString();
    }

    private Map<String, Long> getMapOfTopic(List<Topic> topics) {
        return topics.stream().collect(
                Collectors.toMap(Topic::getTopicName, Topic::getTotalWordsCount));
    }
}
