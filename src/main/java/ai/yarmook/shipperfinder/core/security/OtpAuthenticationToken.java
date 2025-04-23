package ai.yarmook.shipperfinder.core.security;

import ai.yarmook.shipperfinder.core.constant.OtpConstant;
import ai.yarmook.shipperfinder.core.service.OTPService;
import ai.yarmook.shipperfinder.core.service.TwilioService;
import ai.yarmook.shipperfinder.core.service.errors.ExpiredOtpException;
import ai.yarmook.shipperfinder.core.service.errors.IncorrectOtpException;
import ai.yarmook.shipperfinder.core.service.errors.ManyTriesOtpException;
import ai.yarmook.shipperfinder.core.service.errors.NotFoundOtpException;
import java.io.IOException;
import java.util.Collection;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.Assert;

public class OtpAuthenticationToken extends AbstractAuthenticationToken {

    private static final long serialVersionUID = 620L;
    private final Object principal;
    private Object credentials;
    private TwilioService twilioService;
    private OTPService otpService;

    public OtpAuthenticationToken(Object principal, Object credentials, TwilioService twilioService) {
        super((Collection) null);
        this.principal = principal;
        this.credentials = credentials;
        this.twilioService = twilioService;
        this.setAuthenticated(false);
    }

    public OtpAuthenticationToken(
        Object principal,
        Object credentials,
        Collection<? extends GrantedAuthority> authorities,
        TwilioService twilioService
    ) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        this.twilioService = twilioService;
        super.setAuthenticated(twilioService.verifyOtp("+" + this.principal.toString(), this.credentials.toString()));
    }

    public OtpAuthenticationToken(
        Object principal,
        Object credentials,
        Collection<? extends GrantedAuthority> authorities,
        OTPService otpService
    ) throws IOException, NotFoundOtpException, ExpiredOtpException, IncorrectOtpException, ManyTriesOtpException {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        this.otpService = otpService;
        super.setAuthenticated(
            otpService.verifyOtp(this.principal.toString(), this.credentials.toString()).equals(OtpConstant.OTP_SUCCESS)
        );
    }

    public static OtpAuthenticationToken unauthenticated(Object principal, Object credentials) {
        return new OtpAuthenticationToken(principal, credentials, null);
    }

    public static OtpAuthenticationToken authenticated(
        Object principal,
        Object credentials,
        Collection<? extends GrantedAuthority> authorities,
        TwilioService twilioService
    ) {
        return new OtpAuthenticationToken(principal, credentials, authorities, twilioService);
    }

    public static OtpAuthenticationToken authenticated(
        Object principal,
        Object credentials,
        Collection<? extends GrantedAuthority> authorities,
        OTPService otpService
    ) throws IOException, NotFoundOtpException, ExpiredOtpException, IncorrectOtpException, ManyTriesOtpException {
        return new OtpAuthenticationToken(principal, credentials, authorities, otpService);
    }

    public Object getCredentials() {
        return this.credentials;
    }

    public Object getPrincipal() {
        return this.principal;
    }

    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        Assert.isTrue(!isAuthenticated, "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        super.setAuthenticated(false);
    }

    public void eraseCredentials() {
        super.eraseCredentials();
        this.credentials = null;
    }
}
