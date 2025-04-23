package ai.yarmook.shipperfinder.core.dto.vm;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class LoginOTP {

    @NotNull
    @Size(min = 9, max = 50)
    private String phoneNumber;

    @NotNull
    @Size(min = 4, max = 8)
    private String otp;

    private String notificationToken;

    private String deviceCode;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getNotificationToken() {
        return notificationToken;
    }

    public void setNotificationToken(String notificationToken) {
        this.notificationToken = notificationToken;
    }

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

    @Override
    public String toString() {
        return (
            "LoginOTP{" +
            "phoneNumber='" +
            phoneNumber +
            '\'' +
            ", otp='" +
            otp +
            '\'' +
            ", notificationToken='" +
            notificationToken +
            '\'' +
            ", deviceId='" +
            deviceCode +
            '\'' +
            '}'
        );
    }
}
