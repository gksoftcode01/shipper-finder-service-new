package ai.yarmook.shipperfinder.core.service.impl;

import ai.yarmook.shipperfinder.core.constant.YarmookConstant;
import ai.yarmook.shipperfinder.core.repository.YShowNumberHistoryRepository;
import ai.yarmook.shipperfinder.core.service.YShowNumberHistoryService;
import ai.yarmook.shipperfinder.core.util.DateUtil;
import ai.yarmook.shipperfinder.domain.ShowNumberHistory;
import ai.yarmook.shipperfinder.service.AppUserQueryService;
import ai.yarmook.shipperfinder.service.CargoRequestQueryService;
import ai.yarmook.shipperfinder.service.ShowNumberHistoryQueryService;
import ai.yarmook.shipperfinder.service.TripQueryService;
import ai.yarmook.shipperfinder.service.criteria.AppUserCriteria;
import ai.yarmook.shipperfinder.service.criteria.CargoRequestCriteria;
import ai.yarmook.shipperfinder.service.criteria.ShowNumberHistoryCriteria;
import ai.yarmook.shipperfinder.service.criteria.TripCriteria;
import ai.yarmook.shipperfinder.service.dto.AppUserDTO;
import ai.yarmook.shipperfinder.service.dto.CargoRequestDTO;
import ai.yarmook.shipperfinder.service.dto.TripDTO;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import tech.jhipster.service.filter.InstantFilter;
import tech.jhipster.service.filter.UUIDFilter;

@Service
public class YShowNumberHistoryServiceImpl implements YShowNumberHistoryService {

    private final YShowNumberHistoryRepository yShowNumberHistoryRepository;
    private final ShowNumberHistoryQueryService showNumberHistoryQueryService;
    private final TripQueryService tripQueryService;
    private final CargoRequestQueryService cargoRequestQueryService;
    private final AppUserQueryService appUserQueryService;

    public YShowNumberHistoryServiceImpl(
        YShowNumberHistoryRepository yShowNumberHistoryRepository,
        ShowNumberHistoryQueryService showNumberHistoryQueryService,
        TripQueryService tripQueryService,
        CargoRequestQueryService cargoRequestQueryService,
        AppUserQueryService appUserQueryService
    ) {
        this.yShowNumberHistoryRepository = yShowNumberHistoryRepository;
        this.showNumberHistoryQueryService = showNumberHistoryQueryService;
        this.tripQueryService = tripQueryService;
        this.cargoRequestQueryService = cargoRequestQueryService;
        this.appUserQueryService = appUserQueryService;
    }

    private void addShowNumberAction(String userEncId, String entityEncId, Integer entityType) {
        ShowNumberHistory showNumberHistory = new ShowNumberHistory();
        showNumberHistory.setActionByEncId(UUID.fromString(entityEncId));
        showNumberHistory.setEntityType(entityType);
        showNumberHistory.setActionByEncId(UUID.fromString(userEncId));
        showNumberHistory.setCreatedDate(Instant.now());
        yShowNumberHistoryRepository.save(showNumberHistory);
    }

    @Override
    public String getPhoneNumber(String userEncId, String entityEncId, Integer entityType) {
        String phoneNumber = null;
        if (countCurrentAction(userEncId) <= 2) {
            if (entityType == YarmookConstant.TRIP_ENTITY) {
                TripCriteria criteria = new TripCriteria();
                UUIDFilter filter = new UUIDFilter();
                filter.setEquals(UUID.fromString(entityEncId));
                criteria.setEncId(filter);
                List<TripDTO> list = tripQueryService.findByCriteria(criteria, Pageable.unpaged()).toList();
                if (!list.isEmpty()) {
                    phoneNumber = getPhoneNumberByUserUUID(userEncId);
                }
            } else if (entityType == YarmookConstant.CARGO_REQUEST_ENTITY) {
                CargoRequestCriteria criteria = new CargoRequestCriteria();
                UUIDFilter filter = new UUIDFilter();
                filter.setEquals(UUID.fromString(entityEncId));
                criteria.setEncId(filter);
                List<CargoRequestDTO> list = cargoRequestQueryService.findByCriteria(criteria, Pageable.unpaged()).toList();
                if (!list.isEmpty()) {
                    phoneNumber = getPhoneNumberByUserUUID(userEncId);
                }
            }
        }
        addShowNumberAction(userEncId, entityEncId, entityType);
        return phoneNumber;
    }

    private String getPhoneNumberByUserUUID(String userEncId) {
        AppUserCriteria criteria = new AppUserCriteria();
        UUIDFilter filter = new UUIDFilter();
        filter.setEquals(UUID.fromString(userEncId));
        criteria.setEncId(filter);
        List<AppUserDTO> users = appUserQueryService.findByCriteria(criteria, Pageable.unpaged()).toList();
        if (!users.isEmpty()) {
            return users.get(0).getPhoneNumber();
        }
        return null;
    }

    private Long countCurrentAction(String userEncId) {
        ShowNumberHistoryCriteria criteria = new ShowNumberHistoryCriteria();
        UUIDFilter uuidFilter = new UUIDFilter();
        uuidFilter.setEquals(UUID.fromString(userEncId));
        InstantFilter instantFilter = new InstantFilter();
        instantFilter.setLessThanOrEqual(DateUtil.getEndOfDay()).setGreaterThanOrEqual(DateUtil.getFirstOfDay());
        criteria.setActionByEncId(uuidFilter);
        criteria.setCreatedDate(instantFilter);
        return showNumberHistoryQueryService.countByCriteria(criteria);
    }
}
