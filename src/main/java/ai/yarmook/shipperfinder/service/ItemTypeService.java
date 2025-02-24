package ai.yarmook.shipperfinder.service;

import ai.yarmook.shipperfinder.service.dto.ItemTypeDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link ai.yarmook.shipperfinder.domain.ItemType}.
 */
public interface ItemTypeService {
    /**
     * Save a itemType.
     *
     * @param itemTypeDTO the entity to save.
     * @return the persisted entity.
     */
    ItemTypeDTO save(ItemTypeDTO itemTypeDTO);

    /**
     * Updates a itemType.
     *
     * @param itemTypeDTO the entity to update.
     * @return the persisted entity.
     */
    ItemTypeDTO update(ItemTypeDTO itemTypeDTO);

    /**
     * Partially updates a itemType.
     *
     * @param itemTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ItemTypeDTO> partialUpdate(ItemTypeDTO itemTypeDTO);

    /**
     * Get the "id" itemType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ItemTypeDTO> findOne(Long id);

    /**
     * Delete the "id" itemType.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
