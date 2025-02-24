package ai.yarmook.shipperfinder.service;

import ai.yarmook.shipperfinder.service.dto.UnitDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link ai.yarmook.shipperfinder.domain.Unit}.
 */
public interface UnitService {
    /**
     * Save a unit.
     *
     * @param unitDTO the entity to save.
     * @return the persisted entity.
     */
    UnitDTO save(UnitDTO unitDTO);

    /**
     * Updates a unit.
     *
     * @param unitDTO the entity to update.
     * @return the persisted entity.
     */
    UnitDTO update(UnitDTO unitDTO);

    /**
     * Partially updates a unit.
     *
     * @param unitDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<UnitDTO> partialUpdate(UnitDTO unitDTO);

    /**
     * Get the "id" unit.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<UnitDTO> findOne(Long id);

    /**
     * Delete the "id" unit.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
