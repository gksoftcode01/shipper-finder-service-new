package ai.yarmook.shipperfinder.core.service.impl;

import ai.yarmook.shipperfinder.core.service.YUserSubscribeService;
import ai.yarmook.shipperfinder.service.UserSubscribeQueryService;
import ai.yarmook.shipperfinder.service.criteria.UserSubscribeCriteria;
import ai.yarmook.shipperfinder.service.dto.UserSubscribeDTO;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.InstantFilter;
import tech.jhipster.service.filter.UUIDFilter;

@Service
public class YUserSubscribeServiceImpl implements YUserSubscribeService {

    private final UserSubscribeQueryService userSubscribeQueryService;

    public YUserSubscribeServiceImpl(UserSubscribeQueryService userSubscribeQueryService) {
        this.userSubscribeQueryService = userSubscribeQueryService;
    }

    @Override
    public UserSubscribeDTO getActiveSubscribeByUser(UUID userId) {
        UserSubscribeCriteria criteria = new UserSubscribeCriteria();
        UUIDFilter uuidFilter = new UUIDFilter();
        uuidFilter.setEquals(userId);
        BooleanFilter activeFilter = new BooleanFilter();
        activeFilter.setEquals(true);
        Instant instant = Instant.now();
        InstantFilter instantFilter = new InstantFilter();
        instantFilter.setGreaterThanOrEqual(instant);
        instantFilter.setLessThanOrEqual(instant.plus(30, ChronoUnit.DAYS));
        List<UserSubscribeDTO> list = userSubscribeQueryService.findByCriteria(criteria, Pageable.unpaged()).toList();
        if (!list.isEmpty()) return list.get(0);
        return null;
    }
}
