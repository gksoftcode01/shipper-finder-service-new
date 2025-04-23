package ai.yarmook.shipperfinder.core.controller;

import ai.yarmook.shipperfinder.core.service.TwilioService;
import ai.yarmook.shipperfinder.domain.Authority;
import ai.yarmook.shipperfinder.domain.User;
import ai.yarmook.shipperfinder.security.AuthoritiesConstants;
import ai.yarmook.shipperfinder.service.UserService;
import ai.yarmook.shipperfinder.service.UsernameAlreadyUsedException;
import ai.yarmook.shipperfinder.service.dto.AdminUserDTO;
import io.swagger.v3.oas.annotations.Operation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/t/otp")
public class TwilioController {

    private final Logger log = LoggerFactory.getLogger(TwilioController.class);

    TwilioService twilioService;
    UserService userService;

    public TwilioController(TwilioService twilioService, UserService userService) {
        this.twilioService = twilioService;
        this.userService = userService;
    }

    @Operation(summary = "Send OTP to phone number")
    @GetMapping("/send/{phoneNumber}")
    public String sendOtp(@PathVariable String phoneNumber) {
        if (!phoneNumber.startsWith("+")) phoneNumber = "+".concat(phoneNumber);
        if (twilioService.sendOtp(phoneNumber)) {
            try {
                registerUser(phoneNumber.replace("+", ""));
            } catch (UsernameAlreadyUsedException usernameAlreadyUsedException) {}
        }
        return "";
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
}
