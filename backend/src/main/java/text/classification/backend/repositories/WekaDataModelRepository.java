package text.classification.backend.repositories;

import text.classification.backend.models.weka.data.WekaDataModel;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface WekaDataModelRepository extends ElasticsearchRepository<WekaDataModel, String> {
    WekaDataModel findByActiveTrue();
}
