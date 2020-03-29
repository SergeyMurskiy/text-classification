package murskiy_sergey.graduate_work.services.reader;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Optional;

public interface TextReader {
    Optional<String> getTextContent(MultipartFile file, String charset) throws Exception;
    Optional<String> getTextContent(File file, String charset) throws Exception;
    String getType();
}
