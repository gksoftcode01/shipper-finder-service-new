package ai.yarmook.shipperfinder.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.yarmook.shipperfinder.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OtpLogDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OtpLogDTO.class);
        OtpLogDTO otpLogDTO1 = new OtpLogDTO();
        otpLogDTO1.setId(1L);
        OtpLogDTO otpLogDTO2 = new OtpLogDTO();
        assertThat(otpLogDTO1).isNotEqualTo(otpLogDTO2);
        otpLogDTO2.setId(otpLogDTO1.getId());
        assertThat(otpLogDTO1).isEqualTo(otpLogDTO2);
        otpLogDTO2.setId(2L);
        assertThat(otpLogDTO1).isNotEqualTo(otpLogDTO2);
        otpLogDTO1.setId(null);
        assertThat(otpLogDTO1).isNotEqualTo(otpLogDTO2);
    }
}
