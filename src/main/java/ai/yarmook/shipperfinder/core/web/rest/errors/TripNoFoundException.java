package ai.yarmook.shipperfinder.core.web.rest.errors;

import ai.yarmook.shipperfinder.web.rest.errors.BadRequestAlertException;
import java.net.URI;

public class TripNoFoundException extends BadRequestAlertException {

    public TripNoFoundException(String defaultMessage, String entityName, String errorKey) {
        super(defaultMessage, entityName, errorKey);
    }

    public TripNoFoundException(URI type, String defaultMessage, String entityName, String errorKey) {
        super(YErrorConstants.TRIP_NOT_FOUND, defaultMessage, entityName, errorKey);
    }
}
