package ai.yarmook.shipperfinder.web.rest;

import ai.yarmook.shipperfinder.repository.ShowNumberHistoryRepository;
import ai.yarmook.shipperfinder.service.ShowNumberHistoryQueryService;
import ai.yarmook.shipperfinder.service.ShowNumberHistoryService;
import ai.yarmook.shipperfinder.service.criteria.ShowNumberHistoryCriteria;
import ai.yarmook.shipperfinder.service.dto.ShowNumberHistoryDTO;
import ai.yarmook.shipperfinder.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link ai.yarmook.shipperfinder.domain.ShowNumberHistory}.
 */
@RestController
@RequestMapping("/api/show-number-histories")
public class ShowNumberHistoryResource {

    private static final Logger LOG = LoggerFactory.getLogger(ShowNumberHistoryResource.class);

    private static final String ENTITY_NAME = "showNumberHistory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ShowNumberHistoryService showNumberHistoryService;

    private final ShowNumberHistoryRepository showNumberHistoryRepository;

    private final ShowNumberHistoryQueryService showNumberHistoryQueryService;

    public ShowNumberHistoryResource(
        ShowNumberHistoryService showNumberHistoryService,
        ShowNumberHistoryRepository showNumberHistoryRepository,
        ShowNumberHistoryQueryService showNumberHistoryQueryService
    ) {
        this.showNumberHistoryService = showNumberHistoryService;
        this.showNumberHistoryRepository = showNumberHistoryRepository;
        this.showNumberHistoryQueryService = showNumberHistoryQueryService;
    }

    /**
     * {@code POST  /show-number-histories} : Create a new showNumberHistory.
     *
     * @param showNumberHistoryDTO the showNumberHistoryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new showNumberHistoryDTO, or with status {@code 400 (Bad Request)} if the showNumberHistory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ShowNumberHistoryDTO> createShowNumberHistory(@RequestBody ShowNumberHistoryDTO showNumberHistoryDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save ShowNumberHistory : {}", showNumberHistoryDTO);
        if (showNumberHistoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new showNumberHistory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        showNumberHistoryDTO = showNumberHistoryService.save(showNumberHistoryDTO);
        return ResponseEntity.created(new URI("/api/show-number-histories/" + showNumberHistoryDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, showNumberHistoryDTO.getId().toString()))
            .body(showNumberHistoryDTO);
    }

    /**
     * {@code PUT  /show-number-histories/:id} : Updates an existing showNumberHistory.
     *
     * @param id the id of the showNumberHistoryDTO to save.
     * @param showNumberHistoryDTO the showNumberHistoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated showNumberHistoryDTO,
     * or with status {@code 400 (Bad Request)} if the showNumberHistoryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the showNumberHistoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ShowNumberHistoryDTO> updateShowNumberHistory(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ShowNumberHistoryDTO showNumberHistoryDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ShowNumberHistory : {}, {}", id, showNumberHistoryDTO);
        if (showNumberHistoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, showNumberHistoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!showNumberHistoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        showNumberHistoryDTO = showNumberHistoryService.update(showNumberHistoryDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, showNumberHistoryDTO.getId().toString()))
            .body(showNumberHistoryDTO);
    }

    /**
     * {@code PATCH  /show-number-histories/:id} : Partial updates given fields of an existing showNumberHistory, field will ignore if it is null
     *
     * @param id the id of the showNumberHistoryDTO to save.
     * @param showNumberHistoryDTO the showNumberHistoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated showNumberHistoryDTO,
     * or with status {@code 400 (Bad Request)} if the showNumberHistoryDTO is not valid,
     * or with status {@code 404 (Not Found)} if the showNumberHistoryDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the showNumberHistoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ShowNumberHistoryDTO> partialUpdateShowNumberHistory(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ShowNumberHistoryDTO showNumberHistoryDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ShowNumberHistory partially : {}, {}", id, showNumberHistoryDTO);
        if (showNumberHistoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, showNumberHistoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!showNumberHistoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ShowNumberHistoryDTO> result = showNumberHistoryService.partialUpdate(showNumberHistoryDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, showNumberHistoryDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /show-number-histories} : get all the showNumberHistories.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of showNumberHistories in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ShowNumberHistoryDTO>> getAllShowNumberHistories(
        ShowNumberHistoryCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ShowNumberHistories by criteria: {}", criteria);

        Page<ShowNumberHistoryDTO> page = showNumberHistoryQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /show-number-histories/count} : count all the showNumberHistories.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countShowNumberHistories(ShowNumberHistoryCriteria criteria) {
        LOG.debug("REST request to count ShowNumberHistories by criteria: {}", criteria);
        return ResponseEntity.ok().body(showNumberHistoryQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /show-number-histories/:id} : get the "id" showNumberHistory.
     *
     * @param id the id of the showNumberHistoryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the showNumberHistoryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ShowNumberHistoryDTO> getShowNumberHistory(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ShowNumberHistory : {}", id);
        Optional<ShowNumberHistoryDTO> showNumberHistoryDTO = showNumberHistoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(showNumberHistoryDTO);
    }

    /**
     * {@code DELETE  /show-number-histories/:id} : delete the "id" showNumberHistory.
     *
     * @param id the id of the showNumberHistoryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShowNumberHistory(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ShowNumberHistory : {}", id);
        showNumberHistoryService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
