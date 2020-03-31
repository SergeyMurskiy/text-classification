package text.classification.backend.controllers;

import text.classification.backend.repositories.WekaDataModelRepository;
import text.classification.common.services.TextAnalyzer;
import text.classification.common.services.reader.TextReaderFacade;
import text.classification.backend.services.weka.WekaDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {
//    @Autowired
//    private TestBean test;
    @Autowired
    private WekaDataService wekaDataService;
    @Autowired
    private WekaDataModelRepository wekaDataModelRepository;

    @Autowired
    private TextReaderFacade textReaderFacade;

    @Autowired
    private TextAnalyzer textAnalyzer;
    @GetMapping("/convert")
    public void coonvert() {
//        File file = null;
//        XWPFWordExtractor extractor = null;
//
//        File dir = new File("/Users/sergejmurskij/Desktop/yyyy/математика");
//        File[] files = dir.listFiles();
//        String fileData = "";
//        assert files != null;
//        for (File file1 : files) {
//            StringBuilder stringBuilder = new StringBuilder();
//
//            try {
//                FileInputStream fis = new FileInputStream(file1.getAbsolutePath());
//                XWPFDocument document = new XWPFDocument(fis);
//                extractor = new XWPFWordExtractor(document);
//                fileData = extractor.getText();
//            } catch (Exception exep) {
//                exep.printStackTrace();
//            }
//
//            assert false;
//            try (FileWriter writer = new FileWriter("texts/" + file1.getName().split("\\.")[0] + ".txt")) {
//                writer.write(fileData);
//                writer.flush();
//            } catch (IOException ex) {
//                ex.printStackTrace();
//            }
//        }

//        File file = new File("test1111.txt");
//        TextAnalyzer.Response textAnalyzerTextTerms = textAnalyzer.getTextTerms(textReaderFacade.getTextContent(file, "UTF-8").get());
//        int a;
    }
}
