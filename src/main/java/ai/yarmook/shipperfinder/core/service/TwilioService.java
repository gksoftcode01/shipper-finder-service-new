package ai.yarmook.shipperfinder.core.service;

public interface TwilioService {
    boolean sendOtp(String phoneNumber);
    boolean verifyOtp(String phoneNumber, String otp);
}
