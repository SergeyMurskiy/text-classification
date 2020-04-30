package text.classification.backend.services.statistic;

import org.springframework.stereotype.Component;

import java.lang.instrument.Instrumentation;
import java.util.HashMap;
import java.util.Map;

@Component
public class MemoryCheck {
    private long startMemory;
    private Map<String, Long> memorycheck;

    MemoryCheck() {
        memorycheck = new HashMap<>();
        startMemory = Runtime.getRuntime().freeMemory();
    }


}