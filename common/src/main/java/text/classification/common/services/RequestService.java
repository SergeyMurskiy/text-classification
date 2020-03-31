package text.classification.common.services;

import text.classification.common.services.classifier.ClassifierRequest;
import text.classification.common.services.learning.LearningRequest;
import text.classification.common.services.reader.TextReaderFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Optional;

@Service
public class RequestService {
    private final TextAnalyzer textAnalyzer;
    private final TextReaderFacade textReaderFacade;

    @Autowired
    public RequestService(TextAnalyzer textAnalyzer, TextReaderFacade textReaderFacade) {
        this.textAnalyzer = textAnalyzer;
        this.textReaderFacade = textReaderFacade;
    }

    public Optional<ClassifierRequest> createClassifierRequest(MultipartFile textFile, String charset) {
        Optional<String> textContent = textReaderFacade.getTextContent(textFile, charset);
        return textContent.map(s -> new ClassifierRequest(textFile.getOriginalFilename(), textAnalyzer.getTextTerms(s)));
    }

    public Optional<ClassifierRequest> createClassifierRequest(File textFile, String charset) {
        Optional<String> textContent = textReaderFacade.getTextContent(textFile, charset);
        return textContent.map(s -> new ClassifierRequest(textFile.getName(), textAnalyzer.getTextTerms(s)));
    }

    public Optional<LearningRequest> createLearningRequest(MultipartFile textFile, String topic, String charset) {
        Optional<String> textContent = textReaderFacade.getTextContent(textFile, charset);
        return textContent.map(s -> new LearningRequest(textFile.getOriginalFilename(), topic, textAnalyzer.getTextTerms(s)));
    }

    public Optional<LearningRequest> createLearningRequest(File textFile, String topic, String charset) {
        Optional<String> textContent = textReaderFacade.getTextContent(textFile, charset);
        return textContent.map(s -> new LearningRequest(textFile.getName(), topic, textAnalyzer.getTextTerms(s)));
    }

}
