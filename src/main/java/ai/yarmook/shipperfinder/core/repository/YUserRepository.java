package ai.yarmook.shipperfinder.core.repository;

import ai.yarmook.shipperfinder.domain.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface YUserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLoginIgnoreCaseAndActivatedTrue(String loginId);
}
