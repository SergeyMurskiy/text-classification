package text.classification.common.services.classifier;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClassifierResponseEntity {
    private String textName;
    private long wordsCount;
    private String classValue;
    private long time;
    private String comment;
}
