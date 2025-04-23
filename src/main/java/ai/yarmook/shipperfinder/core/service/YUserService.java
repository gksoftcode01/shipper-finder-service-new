package ai.yarmook.shipperfinder.core.service;

import ai.yarmook.shipperfinder.domain.User;
import java.util.Optional;

public interface YUserService {
    Optional<User> getUserByLogin(String loginId);

    Optional<User> getUserById(Long id);

    Optional<User> activateRegistrationByOtp(String loginId);
}
