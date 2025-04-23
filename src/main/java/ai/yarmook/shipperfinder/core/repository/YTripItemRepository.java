package ai.yarmook.shipperfinder.core.repository;

import ai.yarmook.shipperfinder.core.model.projection.YTripItem;
import ai.yarmook.shipperfinder.domain.Trip;
import ai.yarmook.shipperfinder.domain.TripItem;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface YTripItemRepository extends JpaRepository<TripItem, Long> {
    @Query(
        value = """
        select tripItem from TripItem tripItem
        left join fetch tripItem.item item
        left join fetch item.itemType itemType
        left join fetch tripItem.tags tags
        left join fetch tripItem.unit unit
        where tripItem.trip.id = :tripId
        """
    )
    Set<YTripItem> listByTrip(Long tripId);

    @Transactional
    @Modifying
    @Query("delete from TripItem t where t.trip.id = :tripId")
    void deleteByTripId(Long tripId);
}
