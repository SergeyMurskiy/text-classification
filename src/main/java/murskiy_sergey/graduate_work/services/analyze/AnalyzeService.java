package murskiy_sergey.graduate_work.services.analyze;

import org.springframework.web.multipart.MultipartFile;

public interface AnalyzeService {
    AnalyzeResponse analyze(MultipartFile[] file, String charset);
    String getName();
}
