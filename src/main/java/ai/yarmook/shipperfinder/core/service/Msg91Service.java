package ai.yarmook.shipperfinder.core.service;

import ai.yarmook.shipperfinder.core.constant.OtpConstant;
import ai.yarmook.shipperfinder.core.service.errors.*;
import ai.yarmook.shipperfinder.domain.Authority;
import ai.yarmook.shipperfinder.domain.User;
import ai.yarmook.shipperfinder.security.AuthoritiesConstants;
import ai.yarmook.shipperfinder.service.OtpLogService;
import ai.yarmook.shipperfinder.service.UserService;
import ai.yarmook.shipperfinder.service.UsernameAlreadyUsedException;
import ai.yarmook.shipperfinder.service.dto.AdminUserDTO;
import ai.yarmook.shipperfinder.service.dto.OtpLogDTO;
import java.io.IOException;
import java.net.URI;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@Qualifier("msg91")
public class Msg91Service implements OTPService {

    private final RestTemplate restTemplate;

    @Value("${ai.yarmook.msg91.authkey}")
    String authkey;

    @Value("${ai.yarmook.msg91.url}")
    String msg91Url;

    @Value("${ai.yarmook.msg91.templateId}")
    String templateId;

    private final YOtpLogService yotpLogService;
    private final UserService userService;
    private final OtpLogService otpLogService;

    private static final Logger LOG = LoggerFactory.getLogger(Msg91Service.class);

    public Msg91Service(RestTemplate restTemplate, YOtpLogService yotpLogService, UserService userService, OtpLogService otpLogService) {
        this.restTemplate = restTemplate;
        this.yotpLogService = yotpLogService;
        this.userService = userService;
        this.otpLogService = otpLogService;
    }

    private String sendSmsOtp(String mobileNumber, String otp) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("authkey", authkey);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        Map<String, Object> body = new HashMap<>();
        body.put("template_id", templateId);
        body.put("short_url", "1");
        body.put("short_url_expiry", "3600");
        body.put("realTimeResponse", "1");

        Map<String, String> recipient = new HashMap<>();
        recipient.put("mobiles", mobileNumber);
        recipient.put("otp", otp);

        body.put("recipients", List.of(recipient));

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(msg91Url, request, String.class);
            LOG.info("MSG91 Response: {} - {}", response.getStatusCodeValue(), response.getBody());
            return response.getBody();
        } catch (Exception e) {
            LOG.error("Failed to send MSG91 message: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public String generateOtp(String mobileNo) throws IOException {
        try {
            registerUser(mobileNo.replace("+", ""));
            OtpLogDTO otpLogDTO = yotpLogService.generateOtp(mobileNo);
            String response = sendSmsOtp(mobileNo, otpLogDTO.getOtpValue());
            otpLogDTO.setResponse(response);
            otpLogDTO.setDelivered(1);
            otpLogDTO.setSendDate(Instant.now());
            otpLogService.save(otpLogDTO);
            return otpLogDTO.getOtpValue();
        } catch (OtpExceedingException e) {
            return OtpConstant.OTP_EXCEEDING;
        }
    }

    @Override
    public String resendOtp(String otp_id) throws IOException {
        return "";
    }

    @Override
    public String verifyOtp(String mobileNo, String otp_code)
        throws IOException, NotFoundOtpException, ExpiredOtpException, IncorrectOtpException, ManyTriesOtpException {
        yotpLogService.validateOtp(mobileNo, otp_code);
        return OtpConstant.OTP_SUCCESS;
    }

    private void registerUser(String phoneNumber) throws UsernameAlreadyUsedException {
        Collection<Authority> authorities = new ArrayList<>();
        Authority authority = new Authority();
        authority.setName(AuthoritiesConstants.ANONYMOUS);
        authorities.add(authority);
        User user = new User();
        user.setLogin(phoneNumber);
        user.setPassword(phoneNumber);
        user.setEmail(phoneNumber.concat("@shipperFinder"));
        user.setAuthorities(authorities.stream().collect(Collectors.toSet()));
        AdminUserDTO userDTO = new AdminUserDTO(user);
        userService.registerUser(userDTO, phoneNumber);
    }

    public void sendNativeOtp(String mobileNumber) {
        // Construct the URL with query parameters
        String url =
            "https://control.msg91.com/api/v5/otp" +
            "?otp_expiry=" +
            10 +
            "&template_id=" +
            "67fd88c2d6fc057503737ea3" +
            "&mobile=" +
            mobileNumber +
            "&authkey=" +
            authkey +
            "&realTimeResponse=1";

        // Set HTTP headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Request body
        Map<String, String> body = new HashMap<>();
        //        body.put("Param1", "value1");
        //        body.put("Param2", "value2");
        //        body.put("Param3", "value3");

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

            System.out.println("Response Code: " + response.getStatusCodeValue());
            System.out.println("Response Body: " + response.getBody());
        } catch (Exception e) {
            System.err.println("Error sending OTP: " + e.getMessage());
        }
    }

    public void verifyNativeOtp(String otp, String mobile) {
        // Build URL with query parameters
        URI uri = UriComponentsBuilder.fromHttpUrl("https://control.msg91.com/api/v5/otp/verify")
            .queryParam("otp", otp)
            .queryParam("mobile", mobile)
            .build()
            .toUri();

        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("authkey", authkey);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);

            System.out.println("Status: " + response.getStatusCodeValue());
            System.out.println("Body: " + response.getBody());
        } catch (Exception e) {
            System.err.println("OTP verification failed: " + e.getMessage());
        }
    }

    public void retryNativeOtp(String mobile) {
        // Build the URL with query parameters
        URI uri = UriComponentsBuilder.fromHttpUrl("https://control.msg91.com/api/v5/otp/retry")
            .queryParam("authkey", authkey)
            .queryParam("retrytype", "text") // e.g. "text" or "voice"
            .queryParam("mobile", mobile)
            .build()
            .toUri();

        // No need for custom headers here (unless MSG91 docs say otherwise)
        HttpEntity<Void> entity = new HttpEntity<>(new HttpHeaders());

        try {
            ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);

            System.out.println("Retry Response Code: " + response.getStatusCodeValue());
            System.out.println("Retry Response Body: " + response.getBody());
        } catch (Exception e) {
            System.err.println("OTP retry failed: " + e.getMessage());
        }
    }
}
