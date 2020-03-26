package murskiy_sergey.graduate_work.services.loader;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TextLoader {
    @Value("${text.to.load.folder}")
    private String textFolderPath;

    public void getTexts() {

    }

}
