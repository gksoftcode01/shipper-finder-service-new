package ai.yarmook.shipperfinder.core.web.rest.errors;

import ai.yarmook.shipperfinder.web.rest.errors.ErrorConstants;
import java.net.URI;

public class YErrorConstants {

    public static final URI APP_USER_NOT_FOUND = URI.create(ErrorConstants.ERR_VALIDATION + "/problem-with-appUser");
    public static final URI CARGO_REQUEST_NOT_FOUND = URI.create(ErrorConstants.ERR_VALIDATION + "/problem-with-appUser");
    public static final URI SUBSCRIBE_TYPE_NOT_FOUND = URI.create(ErrorConstants.ERR_VALIDATION + "/problem-with-appUser");
    public static final URI TRIP_NOT_FOUND = URI.create(ErrorConstants.ERR_VALIDATION + "/problem-with-appUser");
    public static final URI APP_USER_NOT_MATCH_REQUEST_OWNER = URI.create(ErrorConstants.ERR_VALIDATION + "/problem-with-appUser");
    public static final URI OTP_QUOTA_NOT_ENOUGH = URI.create(ErrorConstants.ERR_VALIDATION + "/problem-with-appUser");
}
