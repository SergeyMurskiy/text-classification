package murskiy_sergey.graduate_work.controllers.learning;

import lombok.extern.slf4j.Slf4j;
import murskiy_sergey.graduate_work.services.learning.LearningResponse;
import murskiy_sergey.graduate_work.services.learning.LearningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@Slf4j
@RestController
@RequestMapping(value = "/learning")
public class LearningController {
    private final List<LearningService> learningServiceList;

    @Autowired
    public LearningController(List<LearningService> learningServiceList) {
        this.learningServiceList = learningServiceList;
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public LearningResponse learning(@RequestParam("topic") String topic,
                                         @RequestParam("texts") MultipartFile[] textFiles,
                                         @RequestParam("charset") String charset,
                                         @RequestParam("methodName") String methodName) {
        Optional<LearningService> learningService = learningServiceList.stream()
                .filter(t -> t.getMethodName().equals(methodName))
                .findFirst();

        if (learningService.isPresent()) {
            return learningService.get().learning(textFiles, topic, charset, true);
        } else {
            log.error("Method: " + methodName + " is not present");
            return LearningResponse.builder()
                    .comment("Метод " + methodName + " не найден")
                    .build();
        }
    }
}
