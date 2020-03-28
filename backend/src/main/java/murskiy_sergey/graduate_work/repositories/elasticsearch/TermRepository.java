package murskiy_sergey.graduate_work.repositories.elasticsearch;

import murskiy_sergey.graduate_work.models.term.Term;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface TermRepository extends ElasticsearchRepository<Term, String> {
    List<Term> findByName(String termName);
}
