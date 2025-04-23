package ai.yarmook.shipperfinder.core.repository;

import ai.yarmook.shipperfinder.domain.Trip;
import ai.yarmook.shipperfinder.domain.enumeration.TripStatus;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;

public interface YTripRepository extends JpaRepository<Trip, Long> {
    @Query(
        value = """
        select trip from Trip trip
        left join fetch trip.fromCountry
        left join fetch trip.toCountry
        left join fetch trip.fromState
        left join fetch trip.toState

        where trip.id=:id
        """
    )
    Trip findAllWithRelationships(@Param("id") Long id);

    @Transactional
    @Modifying
    @Query("update Trip t set t.status = :status where t.encId = :encId")
    int updateTripByEncId(@NonNull @Param("status") TripStatus status, @NonNull @Param("encId") UUID encId);

    @Query("select t from Trip t where t.encId = :encId")
    Trip findByEncId(@Param("encId") @NonNull UUID encId);
}
