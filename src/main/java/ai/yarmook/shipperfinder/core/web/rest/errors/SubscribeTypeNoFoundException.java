package ai.yarmook.shipperfinder.core.web.rest.errors;

import ai.yarmook.shipperfinder.web.rest.errors.BadRequestAlertException;
import java.net.URI;

public class SubscribeTypeNoFoundException extends BadRequestAlertException {

    public SubscribeTypeNoFoundException(String defaultMessage, String entityName, String errorKey) {
        super(defaultMessage, entityName, errorKey);
    }

    public SubscribeTypeNoFoundException(URI type, String defaultMessage, String entityName, String errorKey) {
        super(YErrorConstants.SUBSCRIBE_TYPE_NOT_FOUND, defaultMessage, entityName, errorKey);
    }
}
