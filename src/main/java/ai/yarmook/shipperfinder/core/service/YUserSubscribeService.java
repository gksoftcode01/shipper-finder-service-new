package ai.yarmook.shipperfinder.core.service;

import ai.yarmook.shipperfinder.service.dto.UserSubscribeDTO;
import java.util.UUID;

public interface YUserSubscribeService {
    UserSubscribeDTO getActiveSubscribeByUser(UUID userId);
}
