package ai.yarmook.shipperfinder.core.web.rest.errors;

import ai.yarmook.shipperfinder.web.rest.errors.BadRequestAlertException;
import java.net.URI;

public class AppUserNotMatchRequestOwner extends BadRequestAlertException {

    public AppUserNotMatchRequestOwner(String defaultMessage, String entityName, String errorKey) {
        super(defaultMessage, entityName, errorKey);
    }

    public AppUserNotMatchRequestOwner(URI type, String defaultMessage, String entityName, String errorKey) {
        super(YErrorConstants.APP_USER_NOT_MATCH_REQUEST_OWNER, defaultMessage, entityName, errorKey);
    }
}
