package murskiy_sergey.graduate_work.repositories;

import murskiy_sergey.graduate_work.models.statistic.Statistic;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface StatisticRepository extends ElasticsearchRepository<Statistic, String> {
}
