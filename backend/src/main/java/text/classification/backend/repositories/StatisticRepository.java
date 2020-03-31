package text.classification.backend.repositories;

import text.classification.backend.models.statistic.Statistic;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface StatisticRepository extends ElasticsearchRepository<Statistic, String> {
}
