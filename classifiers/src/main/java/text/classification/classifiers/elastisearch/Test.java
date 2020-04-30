package text.classification.classifiers.elastisearch;

import org.springframework.stereotype.Component;
import text.classification.common.services.classification.ClassifierRequest;
import text.classification.common.services.classification.ClassifierResponseEntity;
import text.classification.common.services.classifier.ClassifierConfig;
import text.classification.common.services.classifier.ClassifierModel;
import text.classification.common.services.learning.LearningRequest;
import text.classification.common.services.learning.LearningResponseEntity;

import java.util.*;
import java.util.stream.Collectors;

//@Component
public class Test implements ClassifierModel {
    private Map<String, Long> maxTextCountPerTopic;
    protected Map<String, Term> termInfo;
    protected Map<String, Topic> topicInfo;

    @Override
    public void rebuildClassifier() {
        Topic topic = topicInfo.values().stream()
                .max(Comparator.comparing(Topic::getTotalWordsCount))
                .get();

        maxTextCountPerTopic = Collections.singletonMap(topic.getTopicName(), topic.getTotalWordsCount());
    }

    @Override
    public ClassifierResponseEntity classifier(ClassifierRequest classifierRequest) throws Exception {
        Map<String, Double> result = new HashMap<>();
        long countOfWordsInText = classifierRequest.getCountOfWords();

        List<Topic> topicList = new ArrayList<>(topicInfo.values());

        Map<String, Long> topics = getMapOfTopic(topicList);

        classifierRequest.getTextTerms().forEach((term, count) -> {
            if (termInfo.containsKey(term)) {
                Term savedTerm = termInfo.get(term);

                savedTerm.getTopicsInfo().forEach((topicName, wordsCount) -> {
                    double w = Math.abs((double) wordsCount / topics.get(topicName) -
                            (double) count / countOfWordsInText);
                    if (result.containsKey(topicName)) {
                        double oldValue = result.get(topicName);
                        result.put(topicName, oldValue + w);
                    } else {
                        result.put(topicName, w);
                    }
                });
            }
        });

        result.forEach((key, value) -> {
            if (!maxTextCountPerTopic.containsKey(key)) {
                result.put(key, (double) value / ((List<Long>) maxTextCountPerTopic.values()).get(0));
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
    public LearningResponseEntity updateTrainingData(LearningRequest learningRequest) {
        Map<String, Long> textTerms = learningRequest.getTextTerms();
        textTerms.forEach((term, count) -> {
            if (termInfo.containsKey(term)) {
                Term savedTerm = termInfo.get(term);
                savedTerm.incrementCountOfTexts();
                savedTerm.incrementTopicInfoTermCount(learningRequest.getTopic(), count);
            } else {
                Term termToSave = new Term(term, learningRequest.getTopic(), count);
                termInfo.put(term, termToSave);
            }
        });

        String topic = learningRequest.getTopic();

        if (topicInfo.containsKey(topic)) {
            Topic savedTopic = topicInfo.get(topic);
            savedTopic.updateTopicCounts(1, learningRequest.getCountOfWords());
        } else {
            topicInfo.put(topic, new Topic(topic, 1, learningRequest.getCountOfWords()));
        }

        return new ElasticsearchLearningResponseEntity(learningRequest.getTextName(), learningRequest.getTopic(),
                learningRequest.getCountOfWords());
    }

    @Override
    public void buildClassifier(ClassifierConfig classifierConfig) {
        this.termInfo = new HashMap<>();
        this.topicInfo = new HashMap<>();
    }

    @Override
    public String getClassifierName() {
        return "elasticsearch111";
    }


    protected String createComment(Map<String, Double> result) {
        StringBuilder stringBuilder = new StringBuilder();
        result.forEach((key, value) -> stringBuilder.append(key).append(" : ").append(value).append("\n"));
        return stringBuilder.toString();
    }

    protected Map<String, Long> getMapOfTopic(List<Topic> topics) {
        return topics.stream().collect(
                Collectors.toMap(Topic::getTopicName, Topic::getTotalWordsCount));
    }
}
