package ai.yarmook.shipperfinder.core.service.impl;

import ai.yarmook.shipperfinder.core.repository.YUserRepository;
import ai.yarmook.shipperfinder.core.service.YUserService;
import ai.yarmook.shipperfinder.domain.User;
import ai.yarmook.shipperfinder.repository.UserRepository;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

@Service
public class YUserServiceImpl implements YUserService {

    private final Logger log = LoggerFactory.getLogger(YUserServiceImpl.class);
    private final CacheManager cacheManager;
    private final YUserRepository yUserRepository;
    private final UserRepository userRepository;

    public YUserServiceImpl(CacheManager cacheManager, YUserRepository yUserRepository, UserRepository userRepository) {
        this.cacheManager = cacheManager;
        this.yUserRepository = yUserRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> getUserByLogin(String loginId) {
        return userRepository.findOneByLogin(loginId);
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> activateRegistrationByOtp(String loginId) {
        log.debug("Activating user for activation loginId {}", loginId);
        Optional<User> user = yUserRepository.findByLoginIgnoreCaseAndActivatedTrue(loginId);
        if (user.isPresent()) return user;
        return userRepository
            .findOneByLogin(loginId)
            .map(usr -> {
                // activate given user for the registration key.
                usr.setActivated(true);
                usr.setActivationKey(null);
                //userSearchRepository.save(usr);
                userRepository.save(usr);
                this.clearUserCaches(usr);
                log.debug("Activated user: {}", usr);
                return usr;
            });
    }

    private void clearUserCaches(User user) {
        Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_LOGIN_CACHE)).evict(user.getLogin());
        if (user.getEmail() != null) {
            Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_EMAIL_CACHE)).evict(user.getEmail());
        }
    }
}
