package ai.yarmook.shipperfinder.core.service.errors;

public class ManyTriesOtpException extends Exception {

    public ManyTriesOtpException(String message) {
        super(message);
    }
}
