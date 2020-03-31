package text.classification.common.services.classifier;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

public interface ClassifierService {
    ClassifierResponse classifier(MultipartFile[] textFiles, String charset);
    ClassifierResponse classifier(File[] textFiles, String charset);
    ClassifierResponse classifier(List<ClassifierRequest> classifierRequestList);
    String getMethodName();
}
