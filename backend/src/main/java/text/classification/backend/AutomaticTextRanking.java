package text.classification.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages={"text.classification"})
public class AutomaticTextRanking {
    public static void main(String[] args) {
        SpringApplication.run(AutomaticTextRanking.class, args);
    }
}
