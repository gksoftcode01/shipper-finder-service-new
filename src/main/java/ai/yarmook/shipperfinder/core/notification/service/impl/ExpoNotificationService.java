package ai.yarmook.shipperfinder.core.notification.service.impl;

import ai.yarmook.shipperfinder.core.notification.model.ExpoPushMessage;
import ai.yarmook.shipperfinder.core.notification.service.NotificationService;
import ai.yarmook.shipperfinder.core.service.YAppUserDeviceService;
import com.niamedtech.expo.exposerversdk.ExpoPushNotificationClient;
import com.niamedtech.expo.exposerversdk.request.PushNotification;
import com.niamedtech.expo.exposerversdk.response.TicketResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ExpoNotificationService implements NotificationService {

    private final Logger log = LoggerFactory.getLogger(ExpoNotificationService.class);
    private final ExpoPushNotificationClient client;
    private final YAppUserDeviceService userDeviceService;

    public ExpoNotificationService(YAppUserDeviceService userDeviceService) {
        this.userDeviceService = userDeviceService;
        client = ExpoPushNotificationClient.builder().setHttpClient(HttpClients.createDefault()).build();
    }

    @Override
    public void sendMessage(ExpoPushMessage message) {
        List<PushNotification> notifications = new ArrayList<>();
        notifications.add(createPushNotification(message));
        try {
            List<TicketResponse.Ticket> tickets = client.sendPushNotifications(notifications);
            log.info("we got {} tickets", tickets);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private PushNotification createPushNotification(ExpoPushMessage message) {
        PushNotification notification = new PushNotification();
        notification.setBody(message.getBody());
        notification.setPriority(message.getPriority());
        notification.setTitle(message.getTitle());
        // notification.setSubtitle("SubTest");
        List<String> lst = new ArrayList<>();
        lst.add(message.getTo());
        notification.setTo(lst);
        return notification;
    }

    private ExpoPushMessage updateUserNotificationToken(ExpoPushMessage message) {
        return message.to(userDeviceService.getUserNotificationToken(message.getUserId()));
    }
}
