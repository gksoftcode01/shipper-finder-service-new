package ai.yarmook.shipperfinder.service;

import ai.yarmook.shipperfinder.service.dto.OtpLogDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link ai.yarmook.shipperfinder.domain.OtpLog}.
 */
public interface OtpLogService {
    /**
     * Save a otpLog.
     *
     * @param otpLogDTO the entity to save.
     * @return the persisted entity.
     */
    OtpLogDTO save(OtpLogDTO otpLogDTO);

    /**
     * Updates a otpLog.
     *
     * @param otpLogDTO the entity to update.
     * @return the persisted entity.
     */
    OtpLogDTO update(OtpLogDTO otpLogDTO);

    /**
     * Partially updates a otpLog.
     *
     * @param otpLogDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<OtpLogDTO> partialUpdate(OtpLogDTO otpLogDTO);

    /**
     * Get all the otpLogs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<OtpLogDTO> findAll(Pageable pageable);

    /**
     * Get the "id" otpLog.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OtpLogDTO> findOne(Long id);

    /**
     * Delete the "id" otpLog.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
