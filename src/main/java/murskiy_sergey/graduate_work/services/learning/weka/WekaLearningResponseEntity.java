package murskiy_sergey.graduate_work.services.learning.weka;

import murskiy_sergey.graduate_work.services.learning.LearningResponseEntity;

import java.util.Map;

public class WekaLearningResponseEntity extends LearningResponseEntity {
    private double[] data;
    private int countOfSetDataValues;

    public WekaLearningResponseEntity(String fileName, int countOfWords, Map<String, Integer> textTerms, long time, String comment, double[] data) {
        super(fileName, countOfWords, textTerms, time, comment);
        this.data = data;
    }

    public WekaLearningResponseEntity(String fileName, int countOfWords, Map<String, Integer> textTerms, long time) {
        super(fileName, countOfWords, textTerms, time);
    }

    public double[] getData() {
        return data;
    }

    public void setData(double[] data) {
        this.data = data;
    }

    public int getCountOfSetDataValues() {
        return countOfSetDataValues;
    }

    public void setCountOfSetDataValues(int countOfSetDataValues) {
        this.countOfSetDataValues = countOfSetDataValues;
    }
}
