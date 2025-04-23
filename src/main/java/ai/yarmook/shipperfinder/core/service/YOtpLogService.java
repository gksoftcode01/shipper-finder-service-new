package ai.yarmook.shipperfinder.core.service;

import ai.yarmook.shipperfinder.core.service.errors.*;
import ai.yarmook.shipperfinder.service.dto.OtpLogDTO;

public interface YOtpLogService {
    OtpLogDTO generateOtp(String mobileNumber) throws OtpExceedingException;

    boolean validateOtp(String mobileNumber, String otp)
        throws IncorrectOtpException, ExpiredOtpException, NotFoundOtpException, ManyTriesOtpException;
}
