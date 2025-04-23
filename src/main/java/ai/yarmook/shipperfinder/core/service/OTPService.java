package ai.yarmook.shipperfinder.core.service;

import ai.yarmook.shipperfinder.core.service.errors.ExpiredOtpException;
import ai.yarmook.shipperfinder.core.service.errors.IncorrectOtpException;
import ai.yarmook.shipperfinder.core.service.errors.ManyTriesOtpException;
import ai.yarmook.shipperfinder.core.service.errors.NotFoundOtpException;
import java.io.IOException;

public interface OTPService {
    String generateOtp(String mobileNo) throws IOException;

    String resendOtp(String otp_id) throws IOException;

    String verifyOtp(String mobileNo, String otp_code)
        throws IOException, NotFoundOtpException, ExpiredOtpException, IncorrectOtpException, ManyTriesOtpException;
}
