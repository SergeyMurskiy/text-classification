package murskiy_sergey.graduate_work.services.learning;

import java.util.Map;

public class LearningResponseEntity {
    private String fileName;
    private long countOfWords;
    private Map<String, Long> textTerms;
    private long time;
    private String comment;

    public LearningResponseEntity(String fileName, long countOfWords, Map<String, Long> textTerms, long time, String comment) {
        this.fileName = fileName;
        this.countOfWords = countOfWords;
        this.textTerms = textTerms;
        this.time = time;
        this.comment = comment;
    }

    public LearningResponseEntity(String fileName, long countOfWords, Map<String, Long> textTerms, long time) {
        this.fileName = fileName;
        this.countOfWords = countOfWords;
        this.textTerms = textTerms;
        this.time = time;
    }

    public long getCountOfWords() {
        return countOfWords;
    }

    public void setCountOfWords(long countOfWords) {
        this.countOfWords = countOfWords;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Map<String, Long> getTextTerms() {
        return textTerms;
    }

    public void setTextTerms(Map<String, Long> textTerms) {
        this.textTerms = textTerms;
    }
}
