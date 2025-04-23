package ai.yarmook.shipperfinder.core.service.impl;

import ai.yarmook.shipperfinder.core.dto.YCargoRequestDTO;
import ai.yarmook.shipperfinder.core.model.YCargoRequest;
import ai.yarmook.shipperfinder.core.model.projection.YCargoRequestItem;
import ai.yarmook.shipperfinder.core.repository.YCargoRequestItemRepository;
import ai.yarmook.shipperfinder.core.repository.YCargoRequestRepository;
import ai.yarmook.shipperfinder.core.service.ElasticService;
import ai.yarmook.shipperfinder.core.service.YCargoRequestService;
import ai.yarmook.shipperfinder.core.service.YUserSubscribeService;
import ai.yarmook.shipperfinder.core.web.rest.errors.AppUserNotMatchRequestOwner;
import ai.yarmook.shipperfinder.core.web.rest.errors.CargoRequestNoFoundException;
import ai.yarmook.shipperfinder.domain.CargoRequest;
import ai.yarmook.shipperfinder.domain.CargoRequestItem;
import ai.yarmook.shipperfinder.domain.enumeration.CargoRequestStatus;
import ai.yarmook.shipperfinder.domain.enumeration.SubscribeTypeEnum;
import ai.yarmook.shipperfinder.repository.CargoRequestItemRepository;
import ai.yarmook.shipperfinder.repository.CargoRequestRepository;
import ai.yarmook.shipperfinder.service.CargoRequestQueryService;
import ai.yarmook.shipperfinder.service.criteria.CargoRequestCriteria;
import ai.yarmook.shipperfinder.service.dto.CargoRequestItemDTO;
import ai.yarmook.shipperfinder.service.dto.UserSubscribeDTO;
import ai.yarmook.shipperfinder.service.mapper.CargoRequestItemMapper;
import ai.yarmook.shipperfinder.service.mapper.CargoRequestMapper;
import java.io.IOException;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import tech.jhipster.service.filter.InstantFilter;
import tech.jhipster.service.filter.UUIDFilter;

@Service
public class YCargoRequestServiceImpl implements YCargoRequestService {

    private final CargoRequestRepository cargoRequestRepository;
    private final CargoRequestItemRepository cargoRequestItemRepository;
    private final CargoRequestMapper cargoRequestMapper;
    private final CargoRequestItemMapper cargoRequestItemMapper;
    private final ElasticService elasticService;
    private final YCargoRequestItemRepository yCargoRequestItemRepository;
    private final YCargoRequestRepository yCargoRequestRepository;
    private final CargoRequestQueryService cargoRequestQueryService;
    private final YUserSubscribeService yUserSubscribeService;

    public YCargoRequestServiceImpl(
        CargoRequestRepository cargoRequestRepository,
        CargoRequestItemRepository cargoRequestItemRepository,
        CargoRequestMapper cargoRequestMapper,
        CargoRequestItemMapper cargoRequestItemMapper,
        ElasticService elasticService,
        YCargoRequestItemRepository yCargoRequestItemRepository,
        YCargoRequestRepository yCargoRequestRepository,
        CargoRequestQueryService cargoRequestQueryService,
        YUserSubscribeService yUserSubscribeService
    ) {
        this.cargoRequestRepository = cargoRequestRepository;
        this.cargoRequestItemRepository = cargoRequestItemRepository;
        this.cargoRequestMapper = cargoRequestMapper;
        this.cargoRequestItemMapper = cargoRequestItemMapper;
        this.elasticService = elasticService;
        this.yCargoRequestItemRepository = yCargoRequestItemRepository;
        this.yCargoRequestRepository = yCargoRequestRepository;
        this.cargoRequestQueryService = cargoRequestQueryService;
        this.yUserSubscribeService = yUserSubscribeService;
    }

    @Override
    public Long countRequestByUser(UUID userId, Instant fromDate, Instant toDate) {
        CargoRequestCriteria cargoRequestCriteria = new CargoRequestCriteria();
        UUIDFilter uuidFilter = new UUIDFilter();
        uuidFilter.setEquals(userId);
        InstantFilter dateFilter = new InstantFilter();
        dateFilter.setGreaterThanOrEqual(fromDate).setLessThanOrEqual(toDate);
        cargoRequestCriteria.setCreatedByEncId(uuidFilter);
        cargoRequestCriteria.setCreateDate(dateFilter);
        return cargoRequestQueryService.countByCriteria(cargoRequestCriteria);
    }

    @Override
    public Long canAddByUser(UUID userId, Instant fromDate, Instant toDate) {
        UserSubscribeDTO userSubscribeDTO = yUserSubscribeService.getActiveSubscribeByUser(userId);
        if (userSubscribeDTO != null && userSubscribeDTO.getSubscribeType().getType() != SubscribeTypeEnum.NORMAL) {
            return -1l;
        } else {
            long count = countRequestByUser(userId, fromDate, toDate);
            return 2l - count;
        }
    }

    public YCargoRequestDTO save(YCargoRequestDTO cargoRequestDTO) throws IOException {
        CargoRequest cargoRequest = cargoRequestMapper.toEntity(cargoRequestDTO);
        cargoRequest.setEncId(UUID.randomUUID());
        cargoRequestRepository.save(cargoRequest);
        cargoRequestDTO.setId(cargoRequest.getId());
        Set<CargoRequestItem> items = new HashSet<>();
        for (CargoRequestItemDTO cargoRequestItemDTO : cargoRequestDTO.getItems()) {
            CargoRequestItem item = cargoRequestItemMapper.toEntity(cargoRequestItemDTO);
            item.setCargoRequest(cargoRequest);
            items.add(item);
        }
        cargoRequestItemRepository.saveAll(items);
        if (cargoRequest.getStatus() != CargoRequestStatus.NEW) {
            syncCargoRequest(cargoRequest.getId());
        }
        return cargoRequestDTO;
    }

    @Override
    public UUID cancelRequest(UUID uuid, String appUserEncId) throws CargoRequestNoFoundException, IOException {
        return changeRequestStatus(uuid, appUserEncId, CargoRequestStatus.CANCELLED);
    }

    @Override
    public UUID completeRequest(UUID uuid, String appUserEncId) throws CargoRequestNoFoundException, IOException {
        return changeRequestStatus(uuid, appUserEncId, CargoRequestStatus.COMPLETED);
    }

    private UUID changeRequestStatus(UUID uuid, String appUserEncId, CargoRequestStatus stats)
        throws CargoRequestNoFoundException, IOException {
        CargoRequest request = yCargoRequestRepository.findByEncId(uuid);
        if (request != null) {
            if (request.getCreatedByEncId().toString().equals(appUserEncId)) {
                yCargoRequestRepository.updateCargoStatusByEncId(stats, uuid);
                syncCargoRequest(request.getId());
                return uuid;
            } else {
                throw new AppUserNotMatchRequestOwner("The User is not match the request Owner", "CargoRequest", "AppUser Not Match");
            }
        } else {
            throw new CargoRequestNoFoundException("This Request is not in our database information", "CargoRequest", "notFound");
        }
    }

    @Override
    @Async
    public void syncCargoRequest(Long requestId) throws IOException {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        CargoRequest cargoRequest = cargoRequestRepository.findOneWithEagerRelationships(requestId).orElseThrow();
        Set<YCargoRequestItem> cargoRequestItems = yCargoRequestItemRepository.listByCargoRequest(cargoRequest.getId());
        YCargoRequest yCargoRequest = new YCargoRequest();
        yCargoRequest.setId(cargoRequest.getId());
        yCargoRequest.setFromCountry(cargoRequest.getFromCountry());
        yCargoRequest.setFromState(cargoRequest.getFromState());
        yCargoRequest.setToCountry(cargoRequest.getToCountry());
        yCargoRequest.setToState(cargoRequest.getToState());
        yCargoRequest.setBudget(cargoRequest.getBudget());
        yCargoRequest.setStatus(cargoRequest.getStatus());
        yCargoRequest.setCreatedByEncId(cargoRequest.getCreatedByEncId());
        yCargoRequest.setTakenByEncId(cargoRequest.getTakenByEncId());
        yCargoRequest.setCreateDate(cargoRequest.getCreateDate());
        yCargoRequest.setValidUntil(cargoRequest.getValidUntil());
        yCargoRequest.setItems(cargoRequestItems);
        elasticService.indexCargoRequest(yCargoRequest);
    }

    @Override
    @Async
    public void deleteCargoRequest(Long requestId) throws IOException {
        elasticService.deleteCargoRequest(requestId);
    }
}
