package ai.yarmook.shipperfinder.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class OtpLogTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static OtpLog getOtpLogSample1() {
        return new OtpLog()
            .id(1L)
            .mobileNumber("mobileNumber1")
            .otpValue("otpValue1")
            .delivered(1)
            .verified(1)
            .triesCount(1)
            .response("response1");
    }

    public static OtpLog getOtpLogSample2() {
        return new OtpLog()
            .id(2L)
            .mobileNumber("mobileNumber2")
            .otpValue("otpValue2")
            .delivered(2)
            .verified(2)
            .triesCount(2)
            .response("response2");
    }

    public static OtpLog getOtpLogRandomSampleGenerator() {
        return new OtpLog()
            .id(longCount.incrementAndGet())
            .mobileNumber(UUID.randomUUID().toString())
            .otpValue(UUID.randomUUID().toString())
            .delivered(intCount.incrementAndGet())
            .verified(intCount.incrementAndGet())
            .triesCount(intCount.incrementAndGet())
            .response(UUID.randomUUID().toString());
    }
}
