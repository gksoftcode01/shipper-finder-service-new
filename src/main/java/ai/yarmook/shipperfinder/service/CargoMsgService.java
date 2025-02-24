package ai.yarmook.shipperfinder.service;

import ai.yarmook.shipperfinder.service.dto.CargoMsgDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link ai.yarmook.shipperfinder.domain.CargoMsg}.
 */
public interface CargoMsgService {
    /**
     * Save a cargoMsg.
     *
     * @param cargoMsgDTO the entity to save.
     * @return the persisted entity.
     */
    CargoMsgDTO save(CargoMsgDTO cargoMsgDTO);

    /**
     * Updates a cargoMsg.
     *
     * @param cargoMsgDTO the entity to update.
     * @return the persisted entity.
     */
    CargoMsgDTO update(CargoMsgDTO cargoMsgDTO);

    /**
     * Partially updates a cargoMsg.
     *
     * @param cargoMsgDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CargoMsgDTO> partialUpdate(CargoMsgDTO cargoMsgDTO);

    /**
     * Get the "id" cargoMsg.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CargoMsgDTO> findOne(Long id);

    /**
     * Delete the "id" cargoMsg.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
