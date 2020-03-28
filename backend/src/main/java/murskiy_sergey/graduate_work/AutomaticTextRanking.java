package murskiy_sergey.graduate_work;

import org.apache.log4j.BasicConfigurator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AutomaticTextRanking {
    public static void main(String[] args) {
        BasicConfigurator.configure();
        SpringApplication.run(AutomaticTextRanking.class, args);
    }
}
