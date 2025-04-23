package ai.yarmook.shipperfinder.core.notification.controller;

import ai.yarmook.shipperfinder.core.notification.model.ExpoPushMessage;
import ai.yarmook.shipperfinder.core.notification.service.NotificationService;
import ai.yarmook.shipperfinder.core.notification.service.PushNotificationService;
import com.niamedtech.expo.exposerversdk.request.PushNotification;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notification")
public class PushNotificationController {

    private final PushNotificationService pushNotificationService;
    private final NotificationService notificationService;

    public PushNotificationController(PushNotificationService pushNotificationService, NotificationService notificationService) {
        this.pushNotificationService = pushNotificationService;
        this.notificationService = notificationService;
    }

    @PostMapping("/send-notification")
    public void sendNotification(@RequestBody ExpoPushMessage message) {
        pushNotificationService.sendNotification(message);
    }

    @PostMapping("/expo-send-notification/{userId}")
    public void sendExpoNotification(@RequestBody String body, @PathVariable String userId) {
        ExpoPushMessage message = new ExpoPushMessage().body(body).userId(userId).priority(PushNotification.Priority.NORMAL).title("Hello");
        notificationService.sendMessage(message);
    }
}
