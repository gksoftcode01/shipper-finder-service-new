package ai.yarmook.shipperfinder.service;

import ai.yarmook.shipperfinder.service.dto.AppUserDeviceDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link ai.yarmook.shipperfinder.domain.AppUserDevice}.
 */
public interface AppUserDeviceService {
    /**
     * Save a appUserDevice.
     *
     * @param appUserDeviceDTO the entity to save.
     * @return the persisted entity.
     */
    AppUserDeviceDTO save(AppUserDeviceDTO appUserDeviceDTO);

    /**
     * Updates a appUserDevice.
     *
     * @param appUserDeviceDTO the entity to update.
     * @return the persisted entity.
     */
    AppUserDeviceDTO update(AppUserDeviceDTO appUserDeviceDTO);

    /**
     * Partially updates a appUserDevice.
     *
     * @param appUserDeviceDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AppUserDeviceDTO> partialUpdate(AppUserDeviceDTO appUserDeviceDTO);

    /**
     * Get the "id" appUserDevice.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AppUserDeviceDTO> findOne(Long id);

    /**
     * Delete the "id" appUserDevice.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
