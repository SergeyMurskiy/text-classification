package murskiy_sergey.graduate_work.services.analyze.weka.j48;

import murskiy_sergey.graduate_work.services.analyze.ClassifierRequest;
import murskiy_sergey.graduate_work.services.analyze.ClassifierResponseEntity;
import murskiy_sergey.graduate_work.services.analyze.weka.WekaClassifierService;
import org.springframework.stereotype.Service;
import weka.classifiers.trees.J48;

@Service
public class C45ClassifierService extends WekaClassifierService {
    @Override
    public String getMethodName() {
        return "C4.5";
    }

    @Override
    protected ClassifierResponseEntity classifierText(ClassifierRequest classifierRequest) throws Exception {
        J48 c4_5Classifier = wekaClassifiers.getJ48Classifier();
        int clsLabel = (int) c4_5Classifier.classifyInstance(createInstance(classifierRequest));

        return ClassifierResponseEntity.builder()
                .textName(classifierRequest.getTextName())
                .wordsCount(classifierRequest.getCountOfWords())
                .classValue(getClassNameByIndex(clsLabel))
                .build();

    }
}
