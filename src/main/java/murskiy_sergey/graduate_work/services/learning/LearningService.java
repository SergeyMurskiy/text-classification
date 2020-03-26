package murskiy_sergey.graduate_work.services.learning;

import org.springframework.web.multipart.MultipartFile;

public interface LearningService {
    LearningResponse learning(MultipartFile[] files, String topic, String charset, boolean rebuildClassifier);
}
