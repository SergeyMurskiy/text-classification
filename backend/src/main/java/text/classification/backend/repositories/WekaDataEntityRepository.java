package text.classification.backend.repositories;

import text.classification.backend.models.weka.data.WekaDataEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface WekaDataEntityRepository extends ElasticsearchRepository<WekaDataEntity, String> {
    List<WekaDataEntity> findByModelId(String modelId);
}
