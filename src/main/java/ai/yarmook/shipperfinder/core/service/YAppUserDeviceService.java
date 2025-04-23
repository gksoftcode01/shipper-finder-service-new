package ai.yarmook.shipperfinder.core.service;

import ai.yarmook.shipperfinder.service.dto.AppUserDeviceDTO;

public interface YAppUserDeviceService {
    AppUserDeviceDTO userLogged(String userId, String deviceCode, String notificationToken);

    String getUserNotificationToken(String userId);
}
