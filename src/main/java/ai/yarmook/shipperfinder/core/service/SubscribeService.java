package ai.yarmook.shipperfinder.core.service;

import ai.yarmook.shipperfinder.domain.SubscribeType;
import ai.yarmook.shipperfinder.domain.UserSubscribe;
import ai.yarmook.shipperfinder.domain.enumeration.SubscribeTypeEnum;
import java.util.List;
import java.util.UUID;

public interface SubscribeService {
    UserSubscribe addSubscribe(UUID userEncId, SubscribeTypeEnum subscribeTypeEnum, Integer periodInMonth);

    SubscribeType getSubscribeType(SubscribeTypeEnum subscribeTypeEnum);

    List<UserSubscribe> findActiveSubscribeByUserIdAndDate(UUID userEncId);

    SubscribeType getCurrentUserSubscribe(UUID userEncId);
}
