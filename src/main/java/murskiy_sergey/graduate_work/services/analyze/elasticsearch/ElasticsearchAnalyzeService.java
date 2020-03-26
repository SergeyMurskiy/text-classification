package murskiy_sergey.graduate_work.services.analyze.elasticsearch;

import murskiy_sergey.graduate_work.models.term.Term;
import murskiy_sergey.graduate_work.models.topic.Topic;
import murskiy_sergey.graduate_work.repositories.elasticsearch.TermRepository;
import murskiy_sergey.graduate_work.repositories.elasticsearch.TopicRepository;
import murskiy_sergey.graduate_work.services.TextAnalyzer;
import murskiy_sergey.graduate_work.services.analyze.AnalyzeResponse;
import murskiy_sergey.graduate_work.services.analyze.AnalyzeService;
import murskiy_sergey.graduate_work.services.reader.TextReaderFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ElasticsearchAnalyzeService implements AnalyzeService {
    private TextAnalyzer textAnalyzer;
    private TextReaderFacade textReader;
    private TermRepository termRepository;
    private TopicRepository topicRepository;

    @Autowired
    public ElasticsearchAnalyzeService(TextAnalyzer textAnalyzer, TextReaderFacade textReader, TermRepository termRepository, TopicRepository topicRepository) {
        this.textAnalyzer = textAnalyzer;
        this.textReader = textReader;
        this.termRepository = termRepository;
        this.topicRepository = topicRepository;
    }

    @Override
    public String getName() {
        return "elasticsearch";
    }

    public AnalyzeResponse analyze(MultipartFile[] file, String charset) {
        Map<String, Double> result = new HashMap<>();
        Optional<String> fileContent = textReader.getTextContent(file[0], charset);

        if (!fileContent.isPresent()) return new AnalyzeResponse();

        TextAnalyzer.Response textTerms = textAnalyzer.getTextTerms(fileContent.get());
        long countOfWordsInText = textTerms.getTextWordsCount();

        List<Topic> topicList = new ArrayList<>();
        topicRepository.findAll().forEach(topicList::add);
        long totalDocumentCount = topicList.stream().mapToLong(Topic::getTextsCount).sum();

        Map<String, Long> topics = getMapOfTopic(topicList);
        textTerms.getTextTerms().forEach((term, count) -> {
            List<Term> savedTerms = null;
            try {
                savedTerms = termRepository.findByName(term);
            } catch (Exception e) {
                System.out.println(term);
            }

//            if (savedTerms != null && savedTerms.size() != 0) {
//                Term savedTerm = savedTerms.get(0);
//                savedTerm.getTopicsInfo().forEach((topicName, wordsCount) -> {
//                    double w = Math.abs((double) wordsCount / topics.get(topicName) - (double) count / countOfWordsInText) * 1.0 / (1 + Math.log((double) totalDocumentCount / savedTerm.getCountOfTextContainsTerm()));
//                    if (result.containsKey(topicName)) {
//                        double oldValue = result.get(topicName);
//                        result.put(topicName, oldValue + w);
//                    } else {
//                        result.put(topicName, w);
//                    }
//                });
//            }
        });

        //result.forEach((key, value) -> System.out.println(key + ": " + value));

        return new AnalyzeResponse();
    }

    private Map<String, Long> getMapOfTopic(List<Topic> topics) {
        return topics.stream().collect(
                Collectors.toMap(Topic::getTopicName, Topic::getTotalWordsCount));
    }
}
