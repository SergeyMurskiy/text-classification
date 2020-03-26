package murskiy_sergey.graduate_work.controllers.analyze;

import murskiy_sergey.graduate_work.AutomaticTextRanking;
import murskiy_sergey.graduate_work.services.analyze.AnalyzeResponse;
import murskiy_sergey.graduate_work.services.analyze.elasticsearch.ElasticsearchAnalyzeService;
import murskiy_sergey.graduate_work.services.analyze.weka.j48.J48AnalyzeService;
import murskiy_sergey.graduate_work.services.analyze.weka.naivebayes.NaiveBayesAnalyzeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin
@RestController
@RequestMapping(value = "/analyze")
public class AnalyzeController {

    private final ElasticsearchAnalyzeService elasticsearchAnalyzeService;

    @Autowired
    public AnalyzeController(ElasticsearchAnalyzeService elasticsearchAnalyzeService) {
        this.elasticsearchAnalyzeService = elasticsearchAnalyzeService;
    }

    @RequestMapping(value = "/elasticsearch", method = RequestMethod.POST)
    public String analyzeByElasticsearch(@RequestParam("charset") String charset,
                              @RequestParam("files") MultipartFile[] file) {
        long start = System.currentTimeMillis();
        elasticsearchAnalyzeService.analyze(file, charset);
        long end = System.currentTimeMillis();

        return "success" + (end - start);
    }

//    @RequestMapping(value = "/weka/j48", method = RequestMethod.POST)
//    public AnalyzeResponse analyzeByWekaJ48(@RequestParam("charset") String charset,
//                                         @RequestParam("file") MultipartFile[] file) throws Exception {
//        J48AnalyzeService j48Classifier = AutomaticTextRanking.getApplicationContext().getBean(J48AnalyzeService.class);
//        return j48Classifier.analyze(file, charset);
//    }
//
//    @RequestMapping(value = "/weka/bayes", method = RequestMethod.POST)
//    public AnalyzeResponse analyzeByWekaNaiveBayes(@RequestParam("charset") String charset,
//                                                                   @RequestParam("files") MultipartFile[] files) throws Exception {
//        NaiveBayesAnalyzeService naiveBayesClassifier = AutomaticTextRanking.getApplicationContext().getBean(NaiveBayesAnalyzeService.class);
//        return naiveBayesClassifier.analyze(files, charset);
//    }

//    @Autowired
//    List<AnalyzeService> analyzeServices;
//
//    public List<AnalyzeResponse> fullAnalize(@RequestParam("charset") String charset,
//                                                    @RequestParam("files") MultipartFile[] files) throws Exception {
//
//        return analyzeServices.stream().map(a -> a.analyze(files, charset)).collect(Collectors.toList());
//    }
//
//    public AnalyzeResponse analize(@RequestParam("name") String name,
//                                         @RequestParam("charset") String charset,
//                                             @RequestParam("files") MultipartFile[] files) throws Exception {
//
//        return analyzeServices.stream().filter(a -> a.getName().equals(name)).findFirst().get().analyze(files, charset);
//    }
}
