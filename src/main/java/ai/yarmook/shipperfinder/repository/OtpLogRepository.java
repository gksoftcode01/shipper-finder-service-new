package ai.yarmook.shipperfinder.repository;

import ai.yarmook.shipperfinder.domain.OtpLog;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the OtpLog entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OtpLogRepository extends JpaRepository<OtpLog, Long> {}
