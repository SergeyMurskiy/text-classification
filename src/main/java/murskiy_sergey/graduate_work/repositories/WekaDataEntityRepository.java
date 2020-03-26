package murskiy_sergey.graduate_work.repositories;

import murskiy_sergey.graduate_work.models.weka.data.WekaDataEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface WekaDataEntityRepository extends ElasticsearchRepository<WekaDataEntity, String> {
    List<WekaDataEntity> findByModelId(String modelId);
}
