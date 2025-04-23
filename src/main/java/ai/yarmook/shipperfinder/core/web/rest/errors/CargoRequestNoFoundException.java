package ai.yarmook.shipperfinder.core.web.rest.errors;

import ai.yarmook.shipperfinder.web.rest.errors.BadRequestAlertException;
import java.net.URI;

public class CargoRequestNoFoundException extends BadRequestAlertException {

    public CargoRequestNoFoundException(String defaultMessage, String entityName, String errorKey) {
        super(defaultMessage, entityName, errorKey);
    }

    public CargoRequestNoFoundException(URI type, String defaultMessage, String entityName, String errorKey) {
        super(YErrorConstants.CARGO_REQUEST_NOT_FOUND, defaultMessage, entityName, errorKey);
    }
}
