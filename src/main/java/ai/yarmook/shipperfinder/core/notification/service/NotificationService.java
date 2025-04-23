package ai.yarmook.shipperfinder.core.notification.service;

import ai.yarmook.shipperfinder.core.notification.model.ExpoPushMessage;

public interface NotificationService {
    void sendMessage(ExpoPushMessage message);
}
