package ai.yarmook.shipperfinder.core.web.rest.errors;

import ai.yarmook.shipperfinder.web.rest.errors.BadRequestAlertException;
import java.net.URI;

public class NotEnoughOtpQuota extends BadRequestAlertException {

    public NotEnoughOtpQuota(String defaultMessage, String entityName, String errorKey) {
        super(defaultMessage, entityName, errorKey);
    }

    public NotEnoughOtpQuota(URI type, String defaultMessage, String entityName, String errorKey) {
        super(YErrorConstants.OTP_QUOTA_NOT_ENOUGH, defaultMessage, entityName, errorKey);
    }
}
