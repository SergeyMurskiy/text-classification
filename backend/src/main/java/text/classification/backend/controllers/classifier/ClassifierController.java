package text.classification.backend.controllers.classifier;

import lombok.extern.slf4j.Slf4j;
import text.classification.common.services.classifier.ClassifierResponse;
import text.classification.common.services.classifier.ClassifierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@Slf4j
@RequestMapping(value = "/classifier")
public class ClassifierController {
    private final List<ClassifierService> classifierServices;

    @Autowired
    public ClassifierController(List<ClassifierService> classifierServices) {
        this.classifierServices = classifierServices;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ClassifierResponse classifier(@RequestParam("texts") MultipartFile[] textFiles,
                                         @RequestParam("charset") String charset,
                                         @RequestParam("methodName") String methodName) {
        Optional<ClassifierService> classifier = classifierServices.stream()
                .filter(classifierService -> classifierService.getMethodName().equals(methodName))
                .findFirst();

        if (classifier.isPresent()) {
            return classifier.get().classifier(textFiles, charset);
        } else {
            log.error("Method: " + methodName + " is not present");
            return ClassifierResponse.builder()
                    .comment("Метод " + methodName + " не найден")
                    .build();
        }
    }
}
