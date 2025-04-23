package ai.yarmook.shipperfinder.service.mapper;

import static ai.yarmook.shipperfinder.domain.OtpLogAsserts.*;
import static ai.yarmook.shipperfinder.domain.OtpLogTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OtpLogMapperTest {

    private OtpLogMapper otpLogMapper;

    @BeforeEach
    void setUp() {
        otpLogMapper = new OtpLogMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getOtpLogSample1();
        var actual = otpLogMapper.toEntity(otpLogMapper.toDto(expected));
        assertOtpLogAllPropertiesEquals(expected, actual);
    }
}
