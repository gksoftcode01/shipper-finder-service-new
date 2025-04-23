package ai.yarmook.shipperfinder.core.service.impl;

import ai.yarmook.shipperfinder.core.repository.YUserSubscribeRepository;
import ai.yarmook.shipperfinder.core.service.SubscribeService;
import ai.yarmook.shipperfinder.core.web.rest.errors.SubscribeTypeNoFoundException;
import ai.yarmook.shipperfinder.domain.SubscribeType;
import ai.yarmook.shipperfinder.domain.UserSubscribe;
import ai.yarmook.shipperfinder.domain.enumeration.SubscribeTypeEnum;
import ai.yarmook.shipperfinder.repository.SubscribeTypeRepository;
import ai.yarmook.shipperfinder.service.SubscribeTypeQueryService;
import ai.yarmook.shipperfinder.service.criteria.SubscribeTypeCriteria;
import ai.yarmook.shipperfinder.service.dto.SubscribeTypeDTO;
import ai.yarmook.shipperfinder.service.mapper.SubscribeTypeMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class SubscribeServiceImpl implements SubscribeService {

    private final YUserSubscribeRepository yUserSubscribeTypeRepository;
    private final SubscribeTypeQueryService subscribeTypeQueryService;
    private final SubscribeTypeRepository subscribeTypeRepository;
    private final SubscribeTypeMapper subscribeTypeMapper;

    public SubscribeServiceImpl(
        YUserSubscribeRepository yUserSubscribeTypeRepository,
        SubscribeTypeQueryService subscribeTypeQueryService,
        SubscribeTypeRepository subscribeTypeRepository,
        SubscribeTypeMapper subscribeTypeMapper
    ) {
        this.yUserSubscribeTypeRepository = yUserSubscribeTypeRepository;
        this.subscribeTypeQueryService = subscribeTypeQueryService;
        this.subscribeTypeRepository = subscribeTypeRepository;
        this.subscribeTypeMapper = subscribeTypeMapper;
    }

    @Override
    public UserSubscribe addSubscribe(UUID userEncId, SubscribeTypeEnum subscribeTypeEnum, Integer periodInMonth) {
        SubscribeType subscribeType = getSubscribeType(subscribeTypeEnum);
        Instant now = Instant.now();
        Instant end = Instant.now().plus(periodInMonth, ChronoUnit.MONTHS);
        UserSubscribe userSubscribe = new UserSubscribe();
        userSubscribe.subscribedUserEncId(userEncId).fromDate(now).toDate(end).isActive(true).setSubscribeType(subscribeType);
        return yUserSubscribeTypeRepository.save(userSubscribe);
    }

    @Override
    public SubscribeType getSubscribeType(SubscribeTypeEnum subscribeTypeEnum) {
        SubscribeTypeCriteria subscribeTypeCriteria = new SubscribeTypeCriteria();
        SubscribeTypeCriteria.SubscribeTypeEnumFilter filter = new SubscribeTypeCriteria.SubscribeTypeEnumFilter();
        filter.setEquals(subscribeTypeEnum);
        subscribeTypeCriteria.setType(filter);
        List<SubscribeTypeDTO> list = subscribeTypeQueryService.findByCriteria(subscribeTypeCriteria, Pageable.unpaged()).toList();
        if (list.isEmpty()) {
            throw new SubscribeTypeNoFoundException("This Subscribe Type is not in our database information", "RequestType", "notFound");
        }
        return subscribeTypeMapper.toEntity(list.get(0));
    }

    @Override
    public List<UserSubscribe> findActiveSubscribeByUserIdAndDate(UUID userEncId) {
        return yUserSubscribeTypeRepository.findActiveSubscribeByUserIdAndDate(userEncId, Instant.now());
    }

    @Override
    public SubscribeType getCurrentUserSubscribe(UUID userEncId) {
        List<UserSubscribe> list = findActiveSubscribeByUserIdAndDate(userEncId);
        if (!list.isEmpty()) {
            return subscribeTypeRepository.findById(list.get(0).getSubscribeType().getId()).orElseThrow();
        }
        return null;
    }
}
