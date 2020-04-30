package text.classification.backend.controllers;

import text.classification.backend.models.statistic.Statistic;
import text.classification.backend.services.statistic.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/statistic")
public class StatisticController {
    private final StatisticService statisticService;

    @Autowired
    public StatisticController(StatisticService statisticService) {
        this.statisticService = statisticService;
    }

    @RequestMapping("/generate")
    public void generateStatistic() {
        statisticService.generateStatistic(1000, 2000);
    }

    @RequestMapping("/all")
    public List<Statistic> getStatistic() {
        return statisticService.getStatistic();
    }

    @RequestMapping("/delete/all")
    public void deleteAll() {
        statisticService.deleteAllStatistic();
    }

    @RequestMapping(value = "/info/methods", method = RequestMethod.GET)
    public List<String> getAllMethodsName() {
        return statisticService.getAllMethodsName();
    }
}
