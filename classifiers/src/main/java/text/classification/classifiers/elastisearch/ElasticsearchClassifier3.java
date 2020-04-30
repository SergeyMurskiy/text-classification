package text.classification.classifiers.elastisearch;

import org.springframework.stereotype.Component;
import text.classification.common.services.classification.ClassifierRequest;
import text.classification.common.services.classification.ClassifierResponseEntity;

import java.util.*;

//@Component("elasticsearch3")
public class ElasticsearchClassifier3 extends ElasticsearchClassifier {
    @Override
    public String getClassifierName() {
        return "elasticsearch3";
    }

    @Override
    public ClassifierResponseEntity classifier(ClassifierRequest classifierRequest) throws Exception{
        Map<String, Double> result = new HashMap<>();
        long countOfWordsInText = classifierRequest.getCountOfWords();

        List<Topic> topicList = new ArrayList<>(topicInfo.values());

        long totalDocumentCount = topicList.stream()
                .mapToLong(Topic::getTextsCount)
                .sum();

        Map<String, Long> topics = getMapOfTopic(topicList);

        classifierRequest.getTextTerms().forEach((term, count) -> {
            if (termInfo.containsKey(term)) {
                Term savedTerm = termInfo.get(term);

                savedTerm.getTopicsInfo().forEach((topicName, wordsCount) -> {
                    double w = Math.abs((double) wordsCount / topics.get(topicName) -
                            (double) count / countOfWordsInText) *
                            1 / (Math.log((double) totalDocumentCount / savedTerm.getCountOfTexts()));
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
}
