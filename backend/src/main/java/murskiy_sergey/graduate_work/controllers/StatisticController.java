package murskiy_sergey.graduate_work.controllers;

import murskiy_sergey.graduate_work.models.statistic.Statistic;
import murskiy_sergey.graduate_work.services.statistic.StatisticService;
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
        statisticService.generateStatistic(50, 1000);
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
