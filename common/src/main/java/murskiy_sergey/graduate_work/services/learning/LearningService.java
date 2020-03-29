package murskiy_sergey.graduate_work.services.learning;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

public interface LearningService {
    LearningResponse learning(MultipartFile[] files, String topic, String charset, boolean rebuildClassifier);
    LearningResponse learning(File[] files, String topic, String charset, boolean rebuildClassifier);
    LearningResponse learning(List<LearningRequest> learningRequestList, boolean rebuildClassifier);

    String getMethodName();
}
