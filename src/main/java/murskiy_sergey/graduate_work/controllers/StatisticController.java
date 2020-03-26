package murskiy_sergey.graduate_work.controllers;

import murskiy_sergey.graduate_work.models.statistic.Statistic;
import murskiy_sergey.graduate_work.services.statistic.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/statistic")
public class StatisticController {
    @Autowired
    private StatisticService statisticService;

    @RequestMapping("/generate")
    public void generateStatistic() {
        statisticService.generateStatistic(10, 100);
    }

    @RequestMapping("/all")
    public List<Statistic> getStatistic() {
        return statisticService.getStatistic();
    }

    @RequestMapping("/delete/all")
    public void deleteAll() {
        statisticService.deleteAllStatistic();
    }
}
