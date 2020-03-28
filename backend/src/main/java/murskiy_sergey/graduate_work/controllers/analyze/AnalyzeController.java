package murskiy_sergey.graduate_work.controllers.analyze;

import murskiy_sergey.graduate_work.services.analyze.elasticsearch.ElasticsearchClassifierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping(value = "/analyze")
public class AnalyzeController {

    private final ElasticsearchClassifierService elasticsearchClassifierService;

    @Autowired
    public AnalyzeController(ElasticsearchClassifierService elasticsearchClassifierService) {
        this.elasticsearchClassifierService = elasticsearchClassifierService;
    }

//    @RequestMapping(value = "/elasticsearch", method = RequestMethod.POST)
//    public String analyzeByElasticsearch(@RequestParam("charset") String charset,
//                              @RequestParam("files") MultipartFile[] file) {
//        long start = System.currentTimeMillis();
//        elasticsearchAnalyzeService..analyze(file, charset);
//        long end = System.currentTimeMillis();
//
//        return "success" + (end - start);
//    }

//    @RequestMapping(value = "/weka/j48", method = RequestMethod.POST)
//    public ClassifierResponse analyzeByWekaJ48(@RequestParam("charset") String charset,
//                                         @RequestParam("file") MultipartFile[] file) throws Exception {
//        J48ClassifierService j48Classifier = AutomaticTextRanking.getApplicationContext().getBean(J48ClassifierService.class);
//        return j48Classifier.analyze(file, charset);
//    }
//
//    @RequestMapping(value = "/weka/bayes", method = RequestMethod.POST)
//    public ClassifierResponse analyzeByWekaNaiveBayes(@RequestParam("charset") String charset,
//                                                                   @RequestParam("files") MultipartFile[] files) throws Exception {
//        NaiveBayesClassifierService naiveBayesClassifier = AutomaticTextRanking.getApplicationContext().getBean(NaiveBayesClassifierService.class);
//        return naiveBayesClassifier.analyze(files, charset);
//    }

//    @Autowired
//    List<ClassifierService> analyzeServices;
//
//    public List<ClassifierResponse> fullAnalize(@RequestParam("charset") String charset,
//                                                    @RequestParam("files") MultipartFile[] files) throws Exception {
//
//        return analyzeServices.stream().map(a -> a.analyze(files, charset)).collect(Collectors.toList());
//    }
//
//    public ClassifierResponse analize(@RequestParam("name") String name,
//                                         @RequestParam("charset") String charset,
//                                             @RequestParam("files") MultipartFile[] files) throws Exception {
//
//        return analyzeServices.stream().filter(a -> a.getName().equals(name)).findFirst().get().analyze(files, charset);
//    }
}
