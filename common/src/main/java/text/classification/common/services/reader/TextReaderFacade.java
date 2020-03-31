package text.classification.common.services.reader;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class TextReaderFacade {
    private final List<TextReader> textReaderList;

    @Autowired
    public TextReaderFacade(List<TextReader> textReaderList) {
        this.textReaderList = textReaderList;
    }

    public Optional<String> getTextContent(MultipartFile file, String charset) {
        Optional<TextReader> textReader = getTextReader(getFileType(file));
        if (textReader.isPresent()) {
            try {
                return textReader.get().getTextContent(file, charset);
            } catch (Exception e) {
                log.error("Cannot read file: " + file.getOriginalFilename());
            }
        }

        return Optional.empty();
    }

    public Optional<String> getTextContent(File file, String charset) {
        Optional<TextReader> textReader = getTextReader(getFileType(file));
        if (textReader.isPresent()) {
            try {
                return textReader.get().getTextContent(file, charset);
            } catch (Exception e) {
                log.error("Cannot read file: " + file.getName());
            }
        }

        return Optional.empty();
    }

    private String getFileType(MultipartFile file) {
        String[] filePathParts = Objects.requireNonNull(file.getOriginalFilename()).split("\\.");
        return filePathParts[filePathParts.length - 1];
    }

    private String getFileType(File file) {
        String[] filePathParts = Objects.requireNonNull(file.getName()).split("\\.");
        return filePathParts[filePathParts.length - 1];
    }

    private Optional<TextReader> getTextReader(String type) {
        return textReaderList.stream()
                .filter(t -> t.getType().equals(type))
                .findFirst();
    }
}
