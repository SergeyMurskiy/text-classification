package murskiy_sergey.graduate_work.services.analyze;

import weka.core.Instance;

import java.util.Map;

public class AnalyzeResponseEntity {
    private String fileName;
    private int wordsCount;
    private Map<String, Integer> textTerms;
    private Instance instance;
    private String classValue;
    private long milliseconds;
    private String comment;

    public AnalyzeResponseEntity(String fileName, int wordsCount, String classValue, long milliseconds) {
        this.fileName = fileName;
        this.wordsCount = wordsCount;
        this.classValue = classValue;
        this.milliseconds = milliseconds;
    }

    public AnalyzeResponseEntity(String fileName, int wordsCount, Map<String, Integer> textTerms) {
        this.fileName = fileName;
        this.wordsCount = wordsCount;
        this.textTerms = textTerms;
    }

    public AnalyzeResponseEntity() {
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getWordsCount() {
        return wordsCount;
    }

    public void setWordsCount(int wordsCount) {
        this.wordsCount = wordsCount;
    }

    public String getClassValue() {
        return classValue;
    }

    public void setClassValue(String classValue) {
        this.classValue = classValue;
    }

    public long getMilliseconds() {
        return milliseconds;
    }

    public void setMilliseconds(long milliseconds) {
        this.milliseconds = milliseconds;
    }

    public Map<String, Integer> getTextTerms() {
        return textTerms;
    }

    public void setTextTerms(Map<String, Integer> textTerms) {
        this.textTerms = textTerms;
    }

    public Instance getInstance() {
        return instance;
    }

    public void setInstance(Instance instance) {
        this.instance = instance;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
