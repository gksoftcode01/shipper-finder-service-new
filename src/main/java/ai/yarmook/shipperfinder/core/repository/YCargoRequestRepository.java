package ai.yarmook.shipperfinder.core.repository;

import ai.yarmook.shipperfinder.domain.CargoRequest;
import ai.yarmook.shipperfinder.domain.enumeration.CargoRequestStatus;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;

public interface YCargoRequestRepository extends JpaRepository<CargoRequest, Long> {
    @Transactional
    @Modifying
    @Query("update CargoRequest c set c.status = :cargoStatus where c.encId = :encId")
    int updateCargoStatusByEncId(@NonNull @Param("cargoStatus") CargoRequestStatus status, @NonNull @Param("encId") UUID encId);

    @Query("select t from Trip t where t.encId = :encId")
    CargoRequest findByEncId(@Param("encId") @NonNull UUID encId);
}
