package ai.yarmook.shipperfinder.core.repository;

import ai.yarmook.shipperfinder.domain.OtpLog;
import java.time.Instant;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface YOtpLogRepository extends JpaRepository<OtpLog, Long> {
    @Query(
        """
        select o from OtpLog o
        where o.mobileNumber = :mobileNumber and o.createdDate between :createdDateStart and :createdDateEnd
        order by o.createdDate DESC"""
    )
    List<OtpLog> findByMobileNumberAndCreatedDateBetweenOrderByCreatedDateDesc(
        @Param("mobileNumber") String mobileNumber,
        @Param("createdDateStart") Instant createdDateStart,
        @Param("createdDateEnd") Instant createdDateEnd
    );

    @Query(
        """
        select o from OtpLog o
        where o.mobileNumber = :mobileNumber and o.verified = :verified
        order by o.createdDate DESC"""
    )
    List<OtpLog> findByMobileNumberAndOtpValueAndVerifiedOrderByCreatedDateDesc(
        @Param("mobileNumber") String mobileNumber,
        @Param("verified") Integer verified,
        Pageable pageable
    );
}
