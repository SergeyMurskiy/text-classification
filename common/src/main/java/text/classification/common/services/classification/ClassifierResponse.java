package text.classification.common.services.classification;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ClassifierResponse {
    private List<ClassifierResponseEntity> responseEntities;
    private String classifierName;
    private long textCount;
    private long time;
    private String comment;
}
