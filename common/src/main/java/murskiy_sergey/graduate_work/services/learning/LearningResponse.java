package murskiy_sergey.graduate_work.services.learning;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class LearningResponse {
    private String topic;
    private long countOfTexts;
    private long countOfSavedTexts;
    private long countOfWords;
    private long time;
    private String comment;
    private List<? extends LearningResponseEntity> responseEntities;

    public List<LearningResponseEntity> getResponseEntities() {
        return responseEntities.stream().map(responseEntity -> (LearningResponseEntity)responseEntity).collect(Collectors.toList());
    }

}
