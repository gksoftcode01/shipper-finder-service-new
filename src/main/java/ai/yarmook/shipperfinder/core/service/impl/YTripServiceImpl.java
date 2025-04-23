package ai.yarmook.shipperfinder.core.service.impl;

import ai.yarmook.shipperfinder.core.dto.YTripDTO;
import ai.yarmook.shipperfinder.core.model.YTrip;
import ai.yarmook.shipperfinder.core.model.projection.YTripItem;
import ai.yarmook.shipperfinder.core.repository.YTripItemRepository;
import ai.yarmook.shipperfinder.core.repository.YTripRepository;
import ai.yarmook.shipperfinder.core.service.ElasticService;
import ai.yarmook.shipperfinder.core.service.YTripService;
import ai.yarmook.shipperfinder.core.service.YUserSubscribeService;
import ai.yarmook.shipperfinder.core.web.rest.errors.AppUserNotMatchRequestOwner;
import ai.yarmook.shipperfinder.core.web.rest.errors.CargoRequestNoFoundException;
import ai.yarmook.shipperfinder.core.web.rest.errors.TripNoFoundException;
import ai.yarmook.shipperfinder.domain.Trip;
import ai.yarmook.shipperfinder.domain.TripItem;
import ai.yarmook.shipperfinder.domain.enumeration.SubscribeTypeEnum;
import ai.yarmook.shipperfinder.domain.enumeration.TripStatus;
import ai.yarmook.shipperfinder.repository.TripItemRepository;
import ai.yarmook.shipperfinder.repository.TripRepository;
import ai.yarmook.shipperfinder.service.TripQueryService;
import ai.yarmook.shipperfinder.service.criteria.TripCriteria;
import ai.yarmook.shipperfinder.service.dto.TripItemDTO;
import ai.yarmook.shipperfinder.service.dto.UserSubscribeDTO;
import ai.yarmook.shipperfinder.service.mapper.TripItemMapper;
import ai.yarmook.shipperfinder.service.mapper.TripMapper;
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
public class YTripServiceImpl implements YTripService {

    private final TripRepository tripRepository;
    private final TripItemRepository tripItemRepository;
    private final TripMapper tripMapper;
    private final TripItemMapper tripItemMapper;
    private final ElasticService elasticService;
    private final YTripItemRepository yTripItemRepository;
    private final YTripRepository yTripRepository;
    private final TripQueryService tripQueryService;
    private final YUserSubscribeService yUserSubscribeService;

    public YTripServiceImpl(
        TripRepository tripRepository,
        TripItemRepository tripItemRepository,
        TripMapper tripMapper,
        TripItemMapper tripItemMapper,
        ElasticService elasticService,
        YTripItemRepository yTripItemRepository,
        YTripRepository yTripRepository,
        TripQueryService tripQueryService,
        YUserSubscribeService yUserSubscribeService
    ) {
        this.tripRepository = tripRepository;
        this.tripItemRepository = tripItemRepository;
        this.tripMapper = tripMapper;
        this.tripItemMapper = tripItemMapper;
        this.elasticService = elasticService;
        this.yTripItemRepository = yTripItemRepository;
        this.yTripRepository = yTripRepository;
        this.tripQueryService = tripQueryService;
        this.yUserSubscribeService = yUserSubscribeService;
    }

    @Override
    public Long countTripByUser(UUID userId, Instant fromDate, Instant toDate) {
        TripCriteria tripCriteria = new TripCriteria();
        UUIDFilter uuidFilter = new UUIDFilter();
        uuidFilter.setEquals(userId);
        InstantFilter dateFilter = new InstantFilter();
        dateFilter.setGreaterThanOrEqual(fromDate).setLessThanOrEqual(toDate);
        tripCriteria.setCreatedByEncId(uuidFilter);
        tripCriteria.setCreateDate(dateFilter);
        return tripQueryService.countByCriteria(tripCriteria);
    }

    @Override
    public Long canAddByUser(UUID userId, Instant fromDate, Instant toDate) {
        UserSubscribeDTO userSubscribeDTO = yUserSubscribeService.getActiveSubscribeByUser(userId);
        if (userSubscribeDTO != null && userSubscribeDTO.getSubscribeType().getType() != SubscribeTypeEnum.NORMAL) {
            return -1L;
        } else {
            long count = countTripByUser(userId, fromDate, toDate);
            return 2L - count;
        }
    }

    @Override
    public YTripDTO save(YTripDTO tripDto) throws IOException {
        Trip trip = tripMapper.toEntity(tripDto);
        trip.setEncId(UUID.randomUUID());
        tripRepository.save(trip);
        tripDto.setId(trip.getId());
        Set<TripItem> items = new HashSet<>();
        for (TripItemDTO tripItemDTO : tripDto.getItems()) {
            TripItem tripItem = tripItemMapper.toEntity(tripItemDTO);
            tripItem.setTrip(trip);
            items.add(tripItem);
        }
        tripItemRepository.saveAll(items);
        syncTrip(trip.getId());
        return tripDto;
    }

    @Override
    public UUID cancelTrip(UUID uuid, String appUserEncId) throws TripNoFoundException, IOException {
        return changeTripStatus(uuid, appUserEncId, TripStatus.CANCELLED);
    }

    @Override
    public UUID completeTrip(UUID uuid, String appUserEncId) throws TripNoFoundException, IOException {
        return changeTripStatus(uuid, appUserEncId, TripStatus.COMPLETED);
    }

    private UUID changeTripStatus(UUID uuid, String appUserEncId, TripStatus stats) throws CargoRequestNoFoundException, IOException {
        Trip request = yTripRepository.findByEncId(uuid);
        if (request != null) {
            if (request.getCreatedByEncId().toString().equals(appUserEncId)) {
                yTripRepository.updateTripByEncId(stats, uuid);
                syncTrip(request.getId());
                return uuid;
            } else {
                throw new AppUserNotMatchRequestOwner("The User is not match the request Owner", "Trip", "AppUser Not Match");
            }
        } else {
            throw new TripNoFoundException("This Trip is not in our database information", "Trip", "notFound");
        }
    }

    @Override
    @Async
    public void syncTrip(Long tripId) throws IOException {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Trip trip = tripRepository.findOneWithEagerRelationships(tripId).orElseThrow();
        Set<YTripItem> yItems = yTripItemRepository.listByTrip(trip.getId());
        YTrip yTrip = new YTrip();
        yTrip.setId(trip.getId());
        yTrip.setArriveDate(trip.getArriveDate());
        yTrip.setCreateDate(trip.getCreateDate());
        yTrip.setTripDate(trip.getTripDate());
        yTrip.setToState(trip.getToState());
        yTrip.setToCountry(trip.getToCountry());
        yTrip.setIsNegotiate(trip.getIsNegotiate());
        yTrip.setNotes(trip.getNotes());
        yTrip.setStatus(trip.getStatus());
        yTrip.items(yItems);
        yTrip.setMaxWeight(trip.getMaxWeight());
        yTrip.setFromCountry(trip.getFromCountry());
        yTrip.setFromState(trip.getFromState());
        yTrip.setCreatedByEncId(trip.getCreatedByEncId());
        elasticService.indexTrip(yTrip);
    }

    @Override
    @Async
    public void deleteTrip(Long tripId) throws IOException {
        elasticService.deleteTrip(tripId);
    }
}
