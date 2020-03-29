package murskiy_sergey.graduate_work;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class AutomaticTextRanking {
    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(AutomaticTextRanking.class, args);
        String[] allBeanNames = run.getBeanDefinitionNames();
        for(String beanName : allBeanNames) {
            System.out.println(beanName);
        }
    }
}
