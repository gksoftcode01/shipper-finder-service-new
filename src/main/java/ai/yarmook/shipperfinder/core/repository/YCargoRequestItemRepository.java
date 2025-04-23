package ai.yarmook.shipperfinder.core.repository;

import ai.yarmook.shipperfinder.core.model.projection.YCargoRequestItem;
import ai.yarmook.shipperfinder.domain.CargoRequestItem;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface YCargoRequestItemRepository extends JpaRepository<CargoRequestItem, Long> {
    @Query(
        value = """
        select cargoItem from CargoRequestItem cargoItem
        left join fetch cargoItem.item item
        left join fetch item.itemType itemType
        left join fetch cargoItem.tags tags
        left join fetch cargoItem.unit unit
        where cargoItem.cargoRequest.id = :cargoRequestId
        """
    )
    Set<YCargoRequestItem> listByCargoRequest(Long cargoRequestId);

    @Transactional
    @Modifying
    @Query("delete from CargoRequestItem t where t.cargoRequest.id = :requestId")
    void deleteByRequestId(Long requestId);
}
