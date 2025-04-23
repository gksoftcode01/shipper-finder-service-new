package ai.yarmook.shipperfinder.domain;

import static ai.yarmook.shipperfinder.domain.OtpLogTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.yarmook.shipperfinder.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OtpLogTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OtpLog.class);
        OtpLog otpLog1 = getOtpLogSample1();
        OtpLog otpLog2 = new OtpLog();
        assertThat(otpLog1).isNotEqualTo(otpLog2);

        otpLog2.setId(otpLog1.getId());
        assertThat(otpLog1).isEqualTo(otpLog2);

        otpLog2 = getOtpLogSample2();
        assertThat(otpLog1).isNotEqualTo(otpLog2);
    }
}
