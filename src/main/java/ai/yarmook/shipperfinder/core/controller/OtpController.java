package ai.yarmook.shipperfinder.core.controller;

import ai.yarmook.shipperfinder.core.service.Msg91Service;
import ai.yarmook.shipperfinder.core.service.OTPService;
import ai.yarmook.shipperfinder.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import java.io.IOException;
import java.util.Random;
import java.util.random.RandomGenerator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/otp")
public class OtpController {

    //    private final OTPService otpService;
    private final OTPService otpService;
    private final UserService userService;

    public OtpController(@Qualifier("msg91") OTPService otpService, UserService userService) {
        this.otpService = otpService;
        this.userService = userService;
    }

    //    @Operation(summary = "Send OTP to phone number")
    //    @GetMapping("/send/{phoneNumber}")
    //    public String sendOtp(@PathVariable String phoneNumber) throws IOException {
    //        if (!phoneNumber.startsWith("+")) phoneNumber = "+".concat(phoneNumber);
    //        return otpService.generateOtp(phoneNumber);
    //    }
    @Operation(summary = "Send OTP to phone number")
    @GetMapping("/send/{phoneNumber}")
    public String sendOtp(@PathVariable String phoneNumber) throws IOException {
        if (!phoneNumber.startsWith("+")) phoneNumber = "+".concat(phoneNumber);
        return otpService.generateOtp(phoneNumber);
    }

    public int generateOTP(int length) {
        if (length < 1 || length > 9) throw new IllegalArgumentException("OTP length must be between 1 and 9");
        Random random = new Random();
        int min = (int) Math.pow(10, length - 1);
        int max = (int) Math.pow(10, length) - 1;
        return random.nextInt(max - min + 1) + min;
    }
}
