package ai.yarmook.shipperfinder.core.service.errors;

public class OtpExceedingException extends Exception {

    public OtpExceedingException(String message) {
        super(message);
    }
}
