package murskiy_sergey.graduate_work.services.learning.elasticsearch;

import murskiy_sergey.graduate_work.models.term.Term;
import murskiy_sergey.graduate_work.models.topic.Topic;
import murskiy_sergey.graduate_work.repositories.elasticsearch.TermRepository;
import murskiy_sergey.graduate_work.repositories.elasticsearch.TopicRepository;
import murskiy_sergey.graduate_work.services.TextAnalyzer;
import murskiy_sergey.graduate_work.services.learning.LearningResponse;
import murskiy_sergey.graduate_work.services.learning.LearningService;
import murskiy_sergey.graduate_work.services.reader.TextReaderFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
public class ElasticsearchLearningService implements LearningService {
    private final TextAnalyzer textAnalyzer;
    private final TermRepository termRepository;
    private final TopicRepository topicRepository;
    private final TextReaderFacade textReader;

    @Autowired
    public ElasticsearchLearningService(TextAnalyzer textAnalyzer,
                                        TermRepository termRepository,
                                        TopicRepository topicRepository,
                                        TextReaderFacade textReader) {
        this.textAnalyzer = textAnalyzer;
        this.termRepository = termRepository;
        this.topicRepository = topicRepository;
        this.textReader = textReader;
    }

    public LearningResponse learning(MultipartFile[] files, String topicName, String charset, boolean rebuildClassifier) {
        int countOfWordsInTexts = 0;
        int countOfTexts = 0;
        Map<String, Term> termsToSave = new HashMap<>();

        for (MultipartFile file : files) {
            Optional<String> content = textReader.getTextContent(file, charset);

            if (!content.isPresent()) break;

            TextAnalyzer.Response analyzeResponse = textAnalyzer.getTextTerms(content.get());
            countOfWordsInTexts += analyzeResponse.getTextWordsCount();

            analyzeResponse.getTextTerms().forEach((term, count) -> {
                Term savedTerm = termsToSave.get(term);

                if (savedTerm == null) {
                    List<Term> savedTerms;

                    try {
                        savedTerms = termRepository.findByName(term);
                        if (savedTerms.size() > 1) {
                            System.out.println(savedTerms.size() + " " + term);
                        }
                        savedTerm = savedTerms.size() != 0 ? savedTerms.get(0) : null;
                    } catch (Exception exception) {
                        System.out.println(file.getName() + " " + term);
                    }
                }

                if (savedTerm == null) {
                    Term termToSave = new Term(term, topicName, count);

                    termsToSave.put(term, termToSave);
                } else {
                    savedTerm.incrementCountOfTexts();
                    savedTerm.incrementTopicInfoTermCount(topicName, count);

                    termsToSave.put(term, savedTerm);
                }
            });

            countOfTexts++;
        }

        termRepository.saveAll(termsToSave.values());

        Topic savedTopic = topicRepository.findByTopicName(topicName);

        if (savedTopic == null) {
            topicRepository.save(new Topic(topicName, countOfTexts, countOfWordsInTexts));
        } else {
            savedTopic.updateTopicCounts(countOfTexts, countOfWordsInTexts);
            topicRepository.save(savedTopic);
        }

        return new LearningResponse();

        //return new ESLearningResponse(topicName, countOfTexts, countOfWordsInTexts, termsToSave.size(), 0);
    }
}
