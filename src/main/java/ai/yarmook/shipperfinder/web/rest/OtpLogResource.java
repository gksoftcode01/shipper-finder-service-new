package ai.yarmook.shipperfinder.web.rest;

import ai.yarmook.shipperfinder.repository.OtpLogRepository;
import ai.yarmook.shipperfinder.service.OtpLogService;
import ai.yarmook.shipperfinder.service.dto.OtpLogDTO;
import ai.yarmook.shipperfinder.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link ai.yarmook.shipperfinder.domain.OtpLog}.
 */
@RestController
@RequestMapping("/api/otp-logs")
public class OtpLogResource {

    private static final Logger LOG = LoggerFactory.getLogger(OtpLogResource.class);

    private static final String ENTITY_NAME = "otpLog";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OtpLogService otpLogService;

    private final OtpLogRepository otpLogRepository;

    public OtpLogResource(OtpLogService otpLogService, OtpLogRepository otpLogRepository) {
        this.otpLogService = otpLogService;
        this.otpLogRepository = otpLogRepository;
    }

    /**
     * {@code POST  /otp-logs} : Create a new otpLog.
     *
     * @param otpLogDTO the otpLogDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new otpLogDTO, or with status {@code 400 (Bad Request)} if the otpLog has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<OtpLogDTO> createOtpLog(@Valid @RequestBody OtpLogDTO otpLogDTO) throws URISyntaxException {
        LOG.debug("REST request to save OtpLog : {}", otpLogDTO);
        if (otpLogDTO.getId() != null) {
            throw new BadRequestAlertException("A new otpLog cannot already have an ID", ENTITY_NAME, "idexists");
        }
        otpLogDTO = otpLogService.save(otpLogDTO);
        return ResponseEntity.created(new URI("/api/otp-logs/" + otpLogDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, otpLogDTO.getId().toString()))
            .body(otpLogDTO);
    }

    /**
     * {@code PUT  /otp-logs/:id} : Updates an existing otpLog.
     *
     * @param id the id of the otpLogDTO to save.
     * @param otpLogDTO the otpLogDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated otpLogDTO,
     * or with status {@code 400 (Bad Request)} if the otpLogDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the otpLogDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<OtpLogDTO> updateOtpLog(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody OtpLogDTO otpLogDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update OtpLog : {}, {}", id, otpLogDTO);
        if (otpLogDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, otpLogDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!otpLogRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        otpLogDTO = otpLogService.update(otpLogDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, otpLogDTO.getId().toString()))
            .body(otpLogDTO);
    }

    /**
     * {@code PATCH  /otp-logs/:id} : Partial updates given fields of an existing otpLog, field will ignore if it is null
     *
     * @param id the id of the otpLogDTO to save.
     * @param otpLogDTO the otpLogDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated otpLogDTO,
     * or with status {@code 400 (Bad Request)} if the otpLogDTO is not valid,
     * or with status {@code 404 (Not Found)} if the otpLogDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the otpLogDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OtpLogDTO> partialUpdateOtpLog(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody OtpLogDTO otpLogDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update OtpLog partially : {}, {}", id, otpLogDTO);
        if (otpLogDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, otpLogDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!otpLogRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OtpLogDTO> result = otpLogService.partialUpdate(otpLogDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, otpLogDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /otp-logs} : get all the otpLogs.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of otpLogs in body.
     */
    @GetMapping("")
    public ResponseEntity<List<OtpLogDTO>> getAllOtpLogs(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of OtpLogs");
        Page<OtpLogDTO> page = otpLogService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /otp-logs/:id} : get the "id" otpLog.
     *
     * @param id the id of the otpLogDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the otpLogDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<OtpLogDTO> getOtpLog(@PathVariable("id") Long id) {
        LOG.debug("REST request to get OtpLog : {}", id);
        Optional<OtpLogDTO> otpLogDTO = otpLogService.findOne(id);
        return ResponseUtil.wrapOrNotFound(otpLogDTO);
    }

    /**
     * {@code DELETE  /otp-logs/:id} : delete the "id" otpLog.
     *
     * @param id the id of the otpLogDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOtpLog(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete OtpLog : {}", id);
        otpLogService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
