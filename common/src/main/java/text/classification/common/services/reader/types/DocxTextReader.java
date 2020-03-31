package text.classification.common.services.reader.types;

import text.classification.common.services.reader.TextReader;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Optional;

@Service
public class DocxTextReader implements TextReader {
    private final String READER_TYPE = "docx";

    @Override
    public Optional<String> getTextContent(MultipartFile file, String charset) throws Exception {
        String fileContent;

        XWPFDocument document = new XWPFDocument(file.getInputStream());
        XWPFWordExtractor extractor = new XWPFWordExtractor(document);
        fileContent = extractor.getText();

        return Optional.ofNullable(fileContent);
    }

    @Override
    public Optional<String> getTextContent(File file, String charset) throws Exception {
        String fileContent = null;

        InputStream inputStream = new FileInputStream(file);
        XWPFDocument document = new XWPFDocument(inputStream);
        XWPFWordExtractor extractor = new XWPFWordExtractor(document);
        fileContent = extractor.getText();

        return Optional.ofNullable(fileContent);
    }

    @Override
    public String getType() {
        return READER_TYPE;
    }

}
