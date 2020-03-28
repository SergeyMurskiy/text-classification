package murskiy_sergey.graduate_work.services.reader.types;

import murskiy_sergey.graduate_work.services.reader.TextReader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Optional;

@Service
public class TxtTextReader implements TextReader {
    private final String READER_TYPE = "txt";

    @Override
    public Optional<String> getTextContent(MultipartFile file, String charset) throws IOException {
        String fileContent;

        if (charset.equals("UTF-8")) {
            fileContent = new String(file.getBytes());
        } else {
            fileContent = new String(file.getBytes(), charset);
        }

        return Optional.of(fileContent);
    }

    @Override
    public Optional<String> getTextContent(File file, String charset) throws Exception {
        StringBuilder stringBuilder = new StringBuilder();

        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        String line = bufferedReader.readLine();

        while (line != null) {
            stringBuilder.append(line).append("\n");
            line = bufferedReader.readLine();
        }

        return Optional.of(stringBuilder.toString());
    }

    @Override
    public String getType() {
        return READER_TYPE;
    }
}
