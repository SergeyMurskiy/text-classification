package text.classification.backend.repositories.elasticsearch;

import text.classification.backend.models.term.Term;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface TermRepository extends ElasticsearchRepository<Term, String> {
    List<Term> findByName(String termName);
}
