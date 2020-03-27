package murskiy_sergey.graduate_work.services.analyze.weka.naivebayes;

import murskiy_sergey.graduate_work.services.analyze.ClassifierResponseEntity;
import murskiy_sergey.graduate_work.services.analyze.ClassifierRequest;
import murskiy_sergey.graduate_work.services.analyze.weka.WekaClassifierService;
import org.springframework.stereotype.Service;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.Instance;

import java.util.List;

@Service
public class NaiveBayesClassifierService extends WekaClassifierService {
    @Override
    public String getMethodName() {
        return "naive-bayes";
    }

    @Override
    protected ClassifierResponseEntity classifierText(ClassifierRequest classifierRequest) throws Exception {
        Instance instance = createInstance(classifierRequest);
        NaiveBayes naiveBayes = wekaClassifiers.getNaiveBayesClassifier();

        double[] distribution = naiveBayes.distributionForInstance(instance);

        return ClassifierResponseEntity.builder()
                .textName(classifierRequest.getTextName())
                .wordsCount(classifierRequest.getCountOfWords())
                .classValue(getClassNameByDistribution(distribution))
                .comment(createDistributionComment(distribution))
                .build();
    }

    private String createDistributionComment(double[] distribution) {
        List<String> classes = wekaClassifiers.getClasses();
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < distribution.length; i++) {
            stringBuilder.append(classes.get(i)).append(": ").append((int) (distribution[i] * 100)).append("%\n");
        }

        return stringBuilder.toString();
    }
}
