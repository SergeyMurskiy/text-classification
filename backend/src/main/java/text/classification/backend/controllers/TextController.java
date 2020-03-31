package text.classification.backend.controllers;

import text.classification.backend.models.text.TextModel;
import text.classification.backend.services.TextModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping(value = "/text")
public class TextController {
    private final TextModelService textModelService;

    @Autowired
    public TextController(TextModelService textModelService) {
        this.textModelService = textModelService;
    }

    @RequestMapping(value = "/add")
    public void addText(@RequestParam("files") MultipartFile[] files,
                        @RequestParam("charset") String charset,
                        @RequestParam("topic") String topic) {
        textModelService.saveTexts(files, charset, topic);
    }

    @RequestMapping(value = "/all")
    public List<TextModel> getAllTexts() {
        return textModelService.getAllTexts();
    }

    @RequestMapping(value = "/all/size")
    public int getAllTextsSize() {
        return textModelService.getAllTexts().size();
    }

    @RequestMapping(value = "/delete/all")
    public void deleteAllText() {
        textModelService.deleteAllTexts();
    }

    @RequestMapping(value = "/create/{size}")
    public List<String> create(@PathVariable("size") int size) {
        String[] str = {"история", "медицина"};
        return textModelService.createAttributesByTFIDF(Collections.emptyList(), 20);
    }
}
