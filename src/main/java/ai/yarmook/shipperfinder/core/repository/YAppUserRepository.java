package ai.yarmook.shipperfinder.core.repository;

import ai.yarmook.shipperfinder.domain.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface YAppUserRepository extends JpaRepository<AppUser, Long> {}
