package murskiy_sergey.graduate_work.services.analyze.weka.naivebayes;

import murskiy_sergey.graduate_work.services.analyze.AnalyzeResponse;
import murskiy_sergey.graduate_work.services.analyze.AnalyzeResponseEntity;
import murskiy_sergey.graduate_work.services.analyze.weka.WekaAnalyzeService;
import org.springframework.stereotype.Service;
import weka.classifiers.bayes.NaiveBayes;

import java.util.List;

@Service
public class NaiveBayesAnalyzeService extends WekaAnalyzeService {
    @Override
    public String getName() {
        return "naive-bayes";
    }

    @Override
    protected AnalyzeResponse classifierInstances(List<AnalyzeResponseEntity> analyzeResponseEntities) {
        NaiveBayes naiveBayes = wekaClassifiers.getNaiveBayesClassifier();

        analyzeResponseEntities.forEach(analyzeResponseEntity -> {
            try {
                long start = System.currentTimeMillis();
                double[] distribution = naiveBayes.distributionForInstance(analyzeResponseEntity.getInstance());
                long end = System.currentTimeMillis();

                analyzeResponseEntity.setClassValue(getClassNameByClassLabel(getMaxIndex(distribution)));
                analyzeResponseEntity.setMilliseconds(end - start);
                analyzeResponseEntity.setComment(createDistributionComment(distribution));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        return createAnalyzeResponse(analyzeResponseEntities);
    }

    private int getMaxIndex(double[] distribution) {
        double max = 0;
        int index = 0;

        for (int i = 0; i < distribution.length; i++) {
            if(max < distribution[i]) {
                max = distribution[i];
                index = i;
            }
        }

        return index;
    }

    private String createDistributionComment(double[] distribution) {
        List<String> classes = wekaClassifiers.getClasses();
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < distribution.length; i++) {
            stringBuilder.append(classes.get(i)).append(": ").append((int)(distribution[i] * 100)).append("%\n");
        }

        return stringBuilder.toString();
    }
}
