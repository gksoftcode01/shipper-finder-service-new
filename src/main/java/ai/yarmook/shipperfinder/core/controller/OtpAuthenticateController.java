package ai.yarmook.shipperfinder.core.controller;

import static ai.yarmook.shipperfinder.security.SecurityUtils.AUTHORITIES_KEY;
import static ai.yarmook.shipperfinder.security.SecurityUtils.JWT_ALGORITHM;

import ai.yarmook.shipperfinder.core.constant.OtpConstant;
import ai.yarmook.shipperfinder.core.dto.vm.LoginOTP;
import ai.yarmook.shipperfinder.core.security.OtpAuthenticationToken;
import ai.yarmook.shipperfinder.core.service.*;
import ai.yarmook.shipperfinder.core.service.errors.ExpiredOtpException;
import ai.yarmook.shipperfinder.core.service.errors.IncorrectOtpException;
import ai.yarmook.shipperfinder.core.service.errors.ManyTriesOtpException;
import ai.yarmook.shipperfinder.core.service.errors.NotFoundOtpException;
import ai.yarmook.shipperfinder.domain.AppUser;
import ai.yarmook.shipperfinder.domain.SubscribeType;
import ai.yarmook.shipperfinder.domain.User;
import ai.yarmook.shipperfinder.domain.UserSubscribe;
import ai.yarmook.shipperfinder.domain.enumeration.SubscribeTypeEnum;
import ai.yarmook.shipperfinder.security.AuthoritiesConstants;
import ai.yarmook.shipperfinder.service.AppUserQueryService;
import ai.yarmook.shipperfinder.service.AppUserService;
import ai.yarmook.shipperfinder.service.UserService;
import ai.yarmook.shipperfinder.service.criteria.AppUserCriteria;
import ai.yarmook.shipperfinder.service.dto.AppUserDTO;
import ai.yarmook.shipperfinder.service.dto.AppUserDeviceDTO;
import ai.yarmook.shipperfinder.service.impl.AppUserDeviceServiceImpl;
import ai.yarmook.shipperfinder.service.mapper.AppUserMapper;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jose.util.Base64;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.io.IOException;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.UUIDFilter;

@RestController
@RequestMapping("/api")
public class OtpAuthenticateController {

    private final Logger log = LoggerFactory.getLogger(OtpAuthenticateController.class);

    private final TwilioService twilioService;
    private final AppUserQueryService appUserQueryService;
    private final AppUserService appUserService;
    private final AppUserMapper appUserMapper;
    private final YUserService yUserService;
    private final SubscribeService subscribeService;
    private final JwtEncoder jwtEncoder;
    private final YAppUserDeviceService yAppUserDeviceService;
    private final OTPService otpService;

    @Value("${jhipster.security.authentication.jwt.base64-secret}")
    private String jwtKey;

    @Value("${jhipster.security.authentication.jwt.token-validity-in-seconds:0}")
    private long tokenValidityInSeconds;

    @Value("${jhipster.security.authentication.jwt.token-validity-in-seconds-for-remember-me:0}")
    private long tokenValidityInSecondsForRememberMe;

    public OtpAuthenticateController(
        TwilioService twilioService,
        AppUserQueryService appUserQueryService,
        AppUserService appUserService,
        AppUserMapper appUserMapper,
        YUserService yUserService,
        SubscribeService subscribeService,
        JwtEncoder jwtEncoder,
        YAppUserDeviceService yAppUserDeviceService,
        @Qualifier("msg91") OTPService otpService
    ) {
        this.twilioService = twilioService;
        this.appUserQueryService = appUserQueryService;
        this.appUserService = appUserService;
        this.appUserMapper = appUserMapper;
        this.yUserService = yUserService;
        this.subscribeService = subscribeService;
        this.jwtEncoder = jwtEncoder;
        this.yAppUserDeviceService = yAppUserDeviceService;
        this.otpService = otpService;
    }

    private Optional<User> activateUserByOtp(String loginId) {
        return yUserService.activateRegistrationByOtp(loginId);
    }

    private void createAppUser(User user, String deviceCode, String notificationTokem) {
        AppUserDTO appUser = this.getAppUserByUser(user);
        if (appUser == null) {
            AppUser app_user = new AppUser();
            app_user.setUserId(user.getId());
            app_user.setMobileNumber(user.getLogin());
            app_user.setRegisterDate(Instant.now());
            app_user.setEncId(UUID.randomUUID());
            appUserService.save(appUserMapper.toDto(app_user));
            AppUserDeviceDTO appUserDeviceDTO = yAppUserDeviceService.userLogged(
                app_user.getEncId().toString(),
                deviceCode,
                notificationTokem
            );
            log.info("App User Created {} with device {}", app_user, appUserDeviceDTO);
        }
    }

    private AppUserDTO getAppUserByUser(User user) {
        LongFilter filter = new LongFilter();
        filter.setEquals(user.getId());
        AppUserCriteria criteria = new AppUserCriteria();
        criteria.setUserId(filter);
        List<AppUserDTO> list = appUserQueryService.findByCriteria(criteria, PageRequest.of(0, 1)).toList();
        if (!list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }

    private AppUserDTO getAppUserByIdAndEncId(String login, UUID userEncId) {
        User user = yUserService.getUserByLogin(login).orElseThrow();
        LongFilter filter = new LongFilter();
        filter.setEquals(user.getId());
        UUIDFilter uuidFilter = new UUIDFilter();
        uuidFilter.setEquals(userEncId);
        AppUserCriteria criteria = new AppUserCriteria();
        criteria.setUserId(filter);
        criteria.setEncId(uuidFilter);
        List<AppUserDTO> list = appUserQueryService.findByCriteria(criteria, Pageable.unpaged()).toList();
        if (!list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }

    @Operation(summary = "Authenticate user by number and OTP")
    @PostMapping("/authenticate/t/otp")
    public ResponseEntity<JWTToken> authorizeOTPT(@Valid @RequestBody LoginOTP loginOTP) {
        String phoneNumber = loginOTP.getPhoneNumber().replace("+", "");
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(AuthoritiesConstants.USER));
        OtpAuthenticationToken authenticationToken = new OtpAuthenticationToken(phoneNumber, loginOTP.getOtp(), authorities, twilioService);
        if (authenticationToken.isAuthenticated()) {
            Optional<User> user = activateUserByOtp(phoneNumber);
            createAppUser(user.orElseThrow(), loginOTP.getDeviceCode(), loginOTP.getNotificationToken());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            String jwt = this.createToken(authenticationToken, user.orElseThrow().getLogin(), true);
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setBearerAuth(jwt);
            return new ResponseEntity<>(new JWTToken(jwt), httpHeaders, HttpStatus.OK);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Authenticate user by number and OTP")
    @PostMapping("/authenticate/otp")
    public ResponseEntity<JWTToken> authorizeOTP(@Valid @RequestBody LoginOTP loginOTP) throws IOException {
        String phoneNumber = loginOTP.getPhoneNumber().replace("+", "");
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(AuthoritiesConstants.USER));
        OtpAuthenticationToken authenticationToken = null;
        try {
            authenticationToken = new OtpAuthenticationToken(phoneNumber, loginOTP.getOtp(), authorities, otpService);
        } catch (NotFoundOtpException e) {
            return ResponseEntity.badRequest().body(new JWTToken(OtpConstant.OTP_NOT_FOUND));
        } catch (ExpiredOtpException e) {
            return ResponseEntity.badRequest().body(new JWTToken(OtpConstant.OTP_EXPIRED));
        } catch (IncorrectOtpException e) {
            return ResponseEntity.badRequest().body(new JWTToken(OtpConstant.OTP_INCORRECT));
        } catch (ManyTriesOtpException e) {
            return ResponseEntity.badRequest().body(new JWTToken(OtpConstant.OTP_MANY_TRIES));
        }
        if (authenticationToken.isAuthenticated()) {
            Optional<User> user = activateUserByOtp(phoneNumber);
            createAppUser(user.orElseThrow(), loginOTP.getDeviceCode(), loginOTP.getNotificationToken());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            String jwt = this.createToken(authenticationToken, user.orElseThrow().getLogin(), true);
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setBearerAuth(jwt);
            return new ResponseEntity<>(new JWTToken(jwt), httpHeaders, HttpStatus.OK);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/authenticate/refreshToken")
    public ResponseEntity<JWTToken> refreshToken(@Valid @RequestBody String oldToken) {
        AppUserDTO appUserDTO = extractDataFromToken(oldToken);
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(AuthoritiesConstants.USER));
        if (appUserDTO != null) {
            User user = yUserService.getUserById(appUserDTO.getUserId()).orElseThrow();
            AppUser appUser = appUserMapper.toEntity(getAppUserByUser(user));
            Instant now = Instant.now();
            Instant validity = now.plus(this.tokenValidityInSeconds, ChronoUnit.SECONDS);
            SubscribeType subscribeType = subscribeService.getCurrentUserSubscribe(appUser.getEncId());
            JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuedAt(now)
                .expiresAt(validity)
                .subject(user.getLogin())
                .claim(AUTHORITIES_KEY, authorities)
                .claim("appUser", appUser != null ? appUser.getEncId() : "")
                .claim("subscribeType", subscribeType != null ? subscribeType.getType() : SubscribeTypeEnum.NORMAL)
                .build();
            JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();
            String jwt = jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setBearerAuth(jwt);
            return new ResponseEntity<>(new JWTToken(jwt), httpHeaders, HttpStatus.OK);
        }
        return ResponseEntity.badRequest().build();
    }

    public String createToken(Authentication authentication, String loginId, boolean rememberMe) {
        String authorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(" "));
        User user = yUserService.getUserByLogin(loginId).orElseThrow();
        AppUser appUser = appUserMapper.toEntity(getAppUserByUser(user));
        Instant now = Instant.now();
        Instant validity;
        if (rememberMe) {
            validity = now.plus(this.tokenValidityInSecondsForRememberMe, ChronoUnit.SECONDS);
        } else {
            validity = now.plus(this.tokenValidityInSeconds, ChronoUnit.SECONDS);
        }
        SubscribeType subscribeType = subscribeService.getCurrentUserSubscribe(appUser.getEncId());
        // @formatter:off
            JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuedAt(now)
                .expiresAt(validity)
                .subject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .claim("appUser",appUser!=null?appUser.getEncId():"")
                .claim("subscribeType",subscribeType!=null?subscribeType.getType():SubscribeTypeEnum.NORMAL)
                .build();
        JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
    }

    //todo unify with security config
    private SecretKey getSecretKey() {
            byte[] keyBytes = Base64.from(jwtKey).decode();
            return new SecretKeySpec(keyBytes, 0, keyBytes.length, JWT_ALGORITHM.getName());
        }

    private AppUserDTO extractDataFromToken(String token) {
            try {
                // Parse the token
                SignedJWT signedJWT = SignedJWT.parse(token);
                MACVerifier verifier = new MACVerifier(getSecretKey());
                if(!signedJWT.verify(verifier)){
                    return null;
                }
                // Extract the claims
                JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
                // Extract standard claims
                String subject = claims.getSubject(); // e.g., user identifier
                String issuer = claims.getIssuer();   // e.g., your-app
                Date expirationTime = claims.getExpirationTime(); // token expiration date
                String userAppEncId = claims.getClaim("appUser").toString();
                return getAppUserByIdAndEncId(subject,UUID.fromString(userAppEncId));
            } catch (ParseException e) {
                System.out.println("Error parsing token: " + e.getMessage());
            }catch (JOSEException e) {
                    throw new RuntimeException(e);
                }
            return null;
        }

    /**
         * Object to return as body in JWT Authentication.
         */
       public static class JWTToken {

            private String idToken;

            JWTToken(String idToken) {
                this.idToken = idToken;
            }

            @JsonProperty("id_token")
            String getIdToken() {
                return idToken;
            }

            void setIdToken(String idToken) {
                this.idToken = idToken;
            }
        }
}
