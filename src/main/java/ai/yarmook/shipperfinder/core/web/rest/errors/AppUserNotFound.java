package ai.yarmook.shipperfinder.core.web.rest.errors;

import ai.yarmook.shipperfinder.web.rest.errors.BadRequestAlertException;
import java.net.URI;

public class AppUserNotFound extends BadRequestAlertException {

    public AppUserNotFound(String defaultMessage, String entityName, String errorKey) {
        super(defaultMessage, entityName, errorKey);
    }

    public AppUserNotFound(URI type, String defaultMessage, String entityName, String errorKey) {
        super(YErrorConstants.APP_USER_NOT_FOUND, defaultMessage, entityName, errorKey);
    }
}
