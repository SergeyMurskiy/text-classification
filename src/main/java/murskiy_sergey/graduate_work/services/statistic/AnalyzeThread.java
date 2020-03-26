package murskiy_sergey.graduate_work.services.statistic;

import murskiy_sergey.graduate_work.models.statistic.Statistic;
import murskiy_sergey.graduate_work.services.analyze.AnalyzeResponse;
import murskiy_sergey.graduate_work.services.analyze.AnalyzeResponseEntity;
import murskiy_sergey.graduate_work.services.analyze.AnalyzeService;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class AnalyzeThread extends Thread {
    private AnalyzeService analyzeService;
    private MultipartFile[] filesToAnalyze;
    private String topic;
    private String charset;
    private List<Statistic> statistic;

    AnalyzeThread(AnalyzeService analyzeService, MultipartFile[] filesToAnalyze, String topic, String charset) {
        this.analyzeService = analyzeService;
        this.filesToAnalyze = filesToAnalyze;
        this.topic = topic;
        this.charset = charset;
    }

    public List<Statistic> getStatistic() {
        return statistic;
    }

    @Override
    public void run() {
        AnalyzeResponse analyzeResponse = analyzeService.analyze(filesToAnalyze, charset);
        statistic = analyzeResponse.getAnalyzeResponseEntities().stream()
                .map(analyzeResponseEntity -> createStatistic(analyzeService.getName(), topic, analyzeResponseEntity))
                .collect(Collectors.toList());
    }

    private Statistic createStatistic(String methodName, String topic, AnalyzeResponseEntity analyzeResponseEntity) {
        String status;
        if (topic.equals(analyzeResponseEntity.getClassValue())) {
            status = "correct";
        } else {
            status = "incorrect";
        }

        return new Statistic(UUID.randomUUID().toString(), new Date(), methodName, StatisticService.Counter.countOfTexts, StatisticService.Counter.countOfWords,
                topic, status, analyzeResponseEntity.getMilliseconds());
    }

}
