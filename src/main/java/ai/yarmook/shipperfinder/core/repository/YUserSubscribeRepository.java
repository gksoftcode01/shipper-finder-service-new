package ai.yarmook.shipperfinder.core.repository;

import ai.yarmook.shipperfinder.domain.UserSubscribe;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface YUserSubscribeRepository extends JpaRepository<UserSubscribe, Long> {
    @Query(
        """
        select u from UserSubscribe u
        where u.subscribedUserEncId = :subscribedUserEncId
        and u.isActive = true
        and u.fromDate <= :currentDate
        and u.toDate >= :currentDate
        order by u.fromDate desc """
    )
    List<UserSubscribe> findActiveSubscribeByUserIdAndDate(
        @Param("subscribedUserEncId") UUID subscribedUserEncId,
        @Param("currentDate") Instant currentDate
    );
}
