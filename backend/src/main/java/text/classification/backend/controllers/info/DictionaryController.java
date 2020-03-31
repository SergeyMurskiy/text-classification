package text.classification.backend.controllers.info;

import text.classification.backend.models.dictionary.DictionaryEntity;
import text.classification.backend.repositories.DictionaryEntityRepository;
import text.classification.common.services.TextAnalyzeResponse;
import text.classification.common.services.TextAnalyzer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/info")
public class DictionaryController {
    @Autowired
    private DictionaryEntityRepository dictionaryEntityRepository;

    @Autowired
    private TextAnalyzer textAnalyzer;

    @RequestMapping(value = "/dictionary", method = RequestMethod.GET)
    public List<DictionaryEntity> getAll() {
        List<DictionaryEntity> dictionaries = new ArrayList<>();
        dictionaryEntityRepository.findAll().forEach(dictionaries::add);
        return dictionaries;
    }

    @RequestMapping(value = "/dictionary/name/{name}", method = RequestMethod.GET)
    public List<DictionaryEntity> getByName(@PathVariable String name) {
        return dictionaryEntityRepository.findByWord(name);
    }

    @RequestMapping(value = "/dictionary/delete/all", method = RequestMethod.GET)
    public List<DictionaryEntity> deleteAll() {
        dictionaryEntityRepository.deleteAll();
        return getAll();
    }

    @RequestMapping(value = "/dictionary/add", method = RequestMethod.GET)
    public String add() {
        String dahl = DictionaryController.class.getClassLoader().getResource("dictionaries/dahl.txt").getPath();
        String ozhegov = DictionaryController.class.getClassLoader().getResource("dictionaries/ozhegov.txt").getPath();
      //  String russian = DictionaryController.class.getClassLoader().getResource("dictionaries/russian-literature.txt").getPath();

        StringBuilder stringBuilder = new StringBuilder();

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(dahl)))) {
            String sub;
            while ((sub = br.readLine()) != null) {
                if (sub.length() > 3) {
                    stringBuilder.append(sub).append("\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(ozhegov)))) {
            String sub;
            while ((sub = br.readLine()) != null) {
                if (sub.length() > 3) {
                    stringBuilder.append(sub).append("\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

//        try (BufferedReader br = new BufferedReader(
//                new InputStreamReader(
//                        new FileInputStream(russian)))) {
//            String sub;
//            while ((sub = br.readLine()) != null) {
//                if (sub.length() > 3) {
//                    stringBuilder.append(sub).append("\n");
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


        TextAnalyzeResponse analyzeResponse = textAnalyzer.getTextTerms(stringBuilder.toString());

        List<DictionaryEntity> dictionaryEntities = new ArrayList<>();
        analyzeResponse.getTextTerms().keySet().forEach(word-> dictionaryEntities.add(new DictionaryEntity(word)));
        dictionaryEntityRepository.saveAll(dictionaryEntities);

        return "ok";
    }

    private List<String> getDahlWords() {
        List<String> res = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream("DALF.txt"), "CP866"))) {
            String sub;
            while ((sub = br.readLine()) != null) {
                String[] str = sub.split(" ");
                if (str.length > 0) {
                    for (String s : str) {
                        if (!s.equals("") && s.length() > 1) {
                            if (Character.isUpperCase(s.charAt(0)) && Character.isUpperCase(s.charAt(1))) {
                                res.add(s);
                            }
                            break;
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return res;
    }

    private List<String> getRussianLiteratureWords() {
        List<String> res = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream("litf-win.txt"), "CP1251"))) {
            String sub;
            while ((sub = br.readLine()) != null) {
                String[] str = sub.split(" ");
                if (str.length > 0) {
                    res.add(str[0]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return res;
    }

    private List<String> getOzhegovWords() {
        List<String> res = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream("OZHEGOV.txt"), "CP1251"))) {
            String sub;
            while ((sub = br.readLine()) != null) {
               String[] str = sub.split("\\|");
               if (str.length > 0) {
                   res.add(str[0]);
               }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return res;
    }

    private void writeResult(String filename, List<String> words) {
        try (FileWriter writer = new FileWriter(filename + ".txt", true)) {
            words.forEach(word -> {
                try {
                    writer.write(word);
                    writer.append('\n');
                    writer.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
