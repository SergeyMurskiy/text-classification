package murskiy_sergey.graduate_work.services.learning;

import java.util.List;
import java.util.stream.Collectors;

public class LearningResponse {
    private String topic;
    private long countOfTexts;
    private long countOfSavedTexts;
    private long countOfWords;
    private long time;
    private String comment;
    private List<? extends LearningResponseEntity> responseEntities;

    public LearningResponse(String topic, long countOfTexts, long countOfWords,
                            long time, String comment, List<LearningResponseEntity> responseEntities) {
        this.topic = topic;
        this.countOfTexts = countOfTexts;
        this.countOfWords = countOfWords;
        this.countOfSavedTexts = responseEntities.size();
        this.time = time;
        this.comment = comment;
    }

    public LearningResponse(String topic, long countOfTexts,
                            long countOfWords, String comment, List<? extends LearningResponseEntity> responseEntities) {
        this.topic = topic;
        this.countOfTexts = countOfTexts;
        this.countOfWords = countOfWords;
        this.comment = comment;
        this.responseEntities = responseEntities;
    }

    public LearningResponse() {}

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public long getCountOfTexts() {
        return countOfTexts;
    }

    public void setCountOfTexts(long countOfTexts) {
        this.countOfTexts = countOfTexts;
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

    public long getCountOfSavedTexts() {
        return countOfSavedTexts;
    }

    public void setCountOfSavedTexts(long countOfSavedTexts) {
        this.countOfSavedTexts = countOfSavedTexts;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<LearningResponseEntity> getResponseEntities() {
        return responseEntities.stream().map(responseEntity -> (LearningResponseEntity)responseEntity).collect(Collectors.toList());
    }

    public void setResponseEntities(List<? extends LearningResponseEntity> responseEntities) {
        this.responseEntities = responseEntities;
    }
}
