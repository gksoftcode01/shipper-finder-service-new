package ai.yarmook.shipperfinder.core.util;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import org.springframework.stereotype.Component;

@Component
public class DateUtil {

    public static Instant getFirstOfDay() {
        return Instant.now().truncatedTo(ChronoUnit.DAYS);
    }

    public static Instant getEndOfDay() {
        return Instant.now().truncatedTo(ChronoUnit.DAYS).plus(1, ChronoUnit.DAYS);
    }
}
