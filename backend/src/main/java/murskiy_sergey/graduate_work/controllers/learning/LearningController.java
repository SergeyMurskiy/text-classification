package murskiy_sergey.graduate_work.controllers.learning;

import murskiy_sergey.graduate_work.services.learning.elasticsearch.ElasticsearchLearningService;
import murskiy_sergey.graduate_work.services.learning.weka.WekaLearningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin
@RestController
@RequestMapping(value = "/learning")
public class LearningController {
    private final ElasticsearchLearningService elasticsearchLearningService;

    @Autowired
    private WekaLearningService wekaLearningService;

    @Autowired
    public LearningController(ElasticsearchLearningService elasticsearchLearningService) {
        this.elasticsearchLearningService = elasticsearchLearningService;
    }

//    @RequestMapping(value = "/elasticsearch", method = RequestMethod.POST)
//    public ESLearningResponse learningAll(@RequestParam("topic") String topic,
//                                          @RequestParam("charset") String charset,
//                                          @RequestParam("files") MultipartFile[] files) {
//        long start = System.currentTimeMillis();
////        learningService.learning(files, "CP1251", topic);
//        ESLearningResponse response = elasticsearchLearningService.learning(files, charset, topic);
//        long end = System.currentTimeMillis();
//        response.setTime(end - start);
//        return response;
//    }

    @RequestMapping(value = "/weka", method = RequestMethod.POST)
    public String learningWeka(@RequestParam("topic") String topic,
                               @RequestParam("files") MultipartFile[] files,
                               @RequestParam("charset") String charset) {
        long start = System.currentTimeMillis();
        wekaLearningService.learning(files, topic, charset, true);
        long end = System.currentTimeMillis();

        return "success" + (end - start);
    }

//    @RequestMapping(value = "/weka/build", method = RequestMethod.GET)
//    public String buildWeka() {
//        long start = System.currentTimeMillis();
////        learningService.learning(files, "CP1251", topic);
//        wekaLearningService.buildAttributesFromArrf();
//        long end = System.currentTimeMillis();
//        return "success" + (end - start);
//    }
}
