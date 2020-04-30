package text.classification.common.services.classifier;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ClassifierConfig {
    private List<String> attributeNames;
    private List<String> textClasses;
}
