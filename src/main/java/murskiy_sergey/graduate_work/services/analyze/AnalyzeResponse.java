package murskiy_sergey.graduate_work.services.analyze;

import java.util.ArrayList;
import java.util.List;

public class AnalyzeResponse {
    private List<AnalyzeResponseEntity> analyzeResponseEntities;
    private int textCount;
    private long milliseconds;
    private String comment;

    public AnalyzeResponse(List<AnalyzeResponseEntity> analyzeResponseEntities, int textCount, long milliseconds) {
        this.analyzeResponseEntities = analyzeResponseEntities;
        this.textCount = textCount;
        this.milliseconds = milliseconds;
    }

    public AnalyzeResponse() {
        analyzeResponseEntities = new ArrayList<>();
    }

    public AnalyzeResponse(String comment) {
        this.comment = comment;
    }

    public List<AnalyzeResponseEntity> getAnalyzeResponseEntities() {
        return analyzeResponseEntities;
    }

    public void setAnalyzeResponseEntities(List<AnalyzeResponseEntity> analyzeResponseEntities) {
        this.analyzeResponseEntities = analyzeResponseEntities;
    }

    public int getTextCount() {
        return textCount;
    }

    public void setTextCount(int textCount) {
        this.textCount = textCount;
    }

    public long getMilliseconds() {
        return milliseconds;
    }

    public void setMilliseconds(long milliseconds) {
        this.milliseconds = milliseconds;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
