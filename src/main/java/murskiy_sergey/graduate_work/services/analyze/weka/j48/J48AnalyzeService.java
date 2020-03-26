package murskiy_sergey.graduate_work.services.analyze.weka.j48;

import murskiy_sergey.graduate_work.services.analyze.AnalyzeResponse;
import murskiy_sergey.graduate_work.services.analyze.AnalyzeResponseEntity;
import murskiy_sergey.graduate_work.services.analyze.weka.WekaAnalyzeService;
import org.springframework.stereotype.Service;
import weka.classifiers.trees.J48;

import java.util.*;

@Service
public class J48AnalyzeService extends WekaAnalyzeService {
    @Override
    public String getName() {
        return "j48";
    }

    @Override
    protected AnalyzeResponse classifierInstances(List<AnalyzeResponseEntity> analyzeResponseEntities) {
        J48 j48Classifier = wekaClassifiers.getJ48Classifier();
        try {
            for (AnalyzeResponseEntity analyzeResponseEntity : analyzeResponseEntities) {
                double clsLabel = j48Classifier.classifyInstance(analyzeResponseEntity.getInstance());
                analyzeResponseEntity.setClassValue(getClassNameByClassLabel(clsLabel));
            }
        } catch (
                Exception e) {
            e.printStackTrace();
        }

        return createAnalyzeResponse(analyzeResponseEntities);
    }
//    public void analyze(MultipartFile file, String charset) {
//        TextAnalyzer.Response textTerms = textAnalyzer.getTermsOfText(txtTextReader.getTextContent(file, charset).get());
//
//        double[] result = new double[attributes.size()];
//        textTerms.getTermsOfText().forEach((term, count) -> {
//            if (attributes.containsKey(term)) {
//                result[attributes.get(term)] = count;
//            }
//        });
//
//        result[attributes.size() - 1] = -1;
//
//
//        List<String> classes = new ArrayList<>();
//        classes.add("история");
//        classes.add("медицина");
//        wekaAttributes.set(wekaAttributes.size() - 1, new Attribute("class", classes));
//
//        Instances unlabeled = new Instances("text", wekaAttributes, 0);
//        unlabeled.setClassIndex(unlabeled.numAttributes() - 1);
//
//        Instance instance = new DenseInstance(attributes.size());
//        for (Attribute attr : wekaAttributes) {
//            instance.setValue(attr, result[attr.index()]);
//        }
//
//        unlabeled.add(instance);
//
//        try {
//            Instances labeled = new Instances(unlabeled);
//
//            for (int i = 0; i < unlabeled.numInstances(); i++) {
//                double clsLabel = classifier.classifyInstance(unlabeled.instance(i));
//                labeled.instance(i).setClassValue(clsLabel);
//            }
//
//            BufferedWriter writer = new BufferedWriter(
//                    new FileWriter("labeled.arff"));
//
//            System.out.println(classes.get((int)labeled.lastInstance().classValue()));
////            writer.write(labeled.toString());
////            writer.newLine();
////            writer.flush();
////            writer.close();
////            writer.close();
//
////            System.out.println(labeled.lastInstance().classValue());
//        } catch (
//                Exception e) {
//            e.printStackTrace();
//        }
//    }

//    private void writeToArrfFile(List<String> words) {
//        words.forEach(word -> {
//            try (FileWriter writer = new FileWriter("dataset.arff", true)) {
//                String text = "@ATTRIBUTE " + word + " NUMERIC";
//                writer.write(text);
//                writer.append('\n');
//                writer.flush();
//            } catch (IOException ex) {
//                System.out.println(ex.getMessage());
//            }
//        });
//    }
//
//    private void writeToFile(List<String> words) {
//        words.forEach(word -> {
//            try (FileWriter writer = new FileWriter("words.txt", true)) {
//                writer.write(word);
//                writer.append('\n');
//                writer.flush();
//            } catch (IOException ex) {
//                System.out.println(ex.getMessage());
//            }
//        });
//    }
}
