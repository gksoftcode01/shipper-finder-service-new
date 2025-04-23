package ai.yarmook.shipperfinder.core.service.impl;

import ai.yarmook.shipperfinder.core.service.TwilioService;
import com.twilio.Twilio;
import com.twilio.rest.verify.v2.service.Verification;
import com.twilio.rest.verify.v2.service.VerificationCheck;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TwilioServiceImpl implements TwilioService {

    @Value("${ai.yarmook.twilio.service.sid}")
    String serviceSid;

    @Value("${ai.yarmook.twilio.account.sid}")
    String accountSid;

    @Value("${ai.yarmook.twilio.account.token}")
    String token;

    @Value("${ai.yarmook.twilio.service.profile}")
    String twilioProfile;

    @Override
    public boolean sendOtp(String phoneNumber) {
        if (twilioProfile.equalsIgnoreCase("dev")) {
            return true;
        }
        Twilio.init(accountSid, token);
        Verification verification = Verification.creator(serviceSid, phoneNumber, "sms").create();
        if (verification.getSid() != null) {
            return true;
        }
        return false;
    }

    @Override
    public boolean verifyOtp(String phoneNumber, String otp) {
        if (twilioProfile.equalsIgnoreCase("dev")) {
            return true;
        }
        Twilio.init(accountSid, token);
        VerificationCheck verificationCheck = VerificationCheck.creator(serviceSid).setTo(phoneNumber).setCode(otp).create();
        return verificationCheck.getStatus().equals(Verification.Status.APPROVED.toString());
    }
}
