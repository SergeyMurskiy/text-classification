package murskiy_sergey.graduate_work.controllers.info;

import murskiy_sergey.graduate_work.models.term.Term;
import murskiy_sergey.graduate_work.repositories.elasticsearch.TermRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/info/term")
public class TermController {
    private final TermRepository termRepository;

    @Autowired
    public TermController(TermRepository termRepository) {
        this.termRepository = termRepository;
    }

    @GetMapping(value = "/all")
    public List<Term> getAllTerms() {
        List<Term> termList = new ArrayList<>();
        termRepository.findAll().forEach(termList::add);
        return termList;
    }

    @GetMapping(value = "/name/{name}")
    public List<Term> getTermsByName(@PathVariable String name) {
        return termRepository.findByName(name);
    }

    @GetMapping(value = "/delete/all")
    public List<Term> deleteAll() {
        termRepository.deleteAll();
        return getAllTerms();
    }
}
