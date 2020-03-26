package murskiy_sergey.graduate_work.repositories;

import murskiy_sergey.graduate_work.models.weka.data.WekaDataModel;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface WekaDataModelRepository extends ElasticsearchRepository<WekaDataModel, String> {
    WekaDataModel findByActiveTrue();
}
