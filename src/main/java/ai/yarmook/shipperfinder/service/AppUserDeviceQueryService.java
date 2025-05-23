package ai.yarmook.shipperfinder.service;

import ai.yarmook.shipperfinder.domain.*; // for static metamodels
import ai.yarmook.shipperfinder.domain.AppUserDevice;
import ai.yarmook.shipperfinder.repository.AppUserDeviceRepository;
import ai.yarmook.shipperfinder.service.criteria.AppUserDeviceCriteria;
import ai.yarmook.shipperfinder.service.dto.AppUserDeviceDTO;
import ai.yarmook.shipperfinder.service.mapper.AppUserDeviceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link AppUserDevice} entities in the database.
 * The main input is a {@link AppUserDeviceCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AppUserDeviceDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AppUserDeviceQueryService extends QueryService<AppUserDevice> {

    private static final Logger LOG = LoggerFactory.getLogger(AppUserDeviceQueryService.class);

    private final AppUserDeviceRepository appUserDeviceRepository;

    private final AppUserDeviceMapper appUserDeviceMapper;

    public AppUserDeviceQueryService(AppUserDeviceRepository appUserDeviceRepository, AppUserDeviceMapper appUserDeviceMapper) {
        this.appUserDeviceRepository = appUserDeviceRepository;
        this.appUserDeviceMapper = appUserDeviceMapper;
    }

    /**
     * Return a {@link Page} of {@link AppUserDeviceDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AppUserDeviceDTO> findByCriteria(AppUserDeviceCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AppUserDevice> specification = createSpecification(criteria);
        return appUserDeviceRepository.findAll(specification, page).map(appUserDeviceMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AppUserDeviceCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<AppUserDevice> specification = createSpecification(criteria);
        return appUserDeviceRepository.count(specification);
    }

    /**
     * Function to convert {@link AppUserDeviceCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AppUserDevice> createSpecification(AppUserDeviceCriteria criteria) {
        Specification<AppUserDevice> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AppUserDevice_.id));
            }
            if (criteria.getDeviceCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDeviceCode(), AppUserDevice_.deviceCode));
            }
            if (criteria.getNotificationToken() != null) {
                specification = specification.and(
                    buildStringSpecification(criteria.getNotificationToken(), AppUserDevice_.notificationToken)
                );
            }
            if (criteria.getLastLogin() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastLogin(), AppUserDevice_.lastLogin));
            }
            if (criteria.getActive() != null) {
                specification = specification.and(buildSpecification(criteria.getActive(), AppUserDevice_.active));
            }
            if (criteria.getUserEncId() != null) {
                specification = specification.and(buildSpecification(criteria.getUserEncId(), AppUserDevice_.userEncId));
            }
        }
        return specification;
    }
}
