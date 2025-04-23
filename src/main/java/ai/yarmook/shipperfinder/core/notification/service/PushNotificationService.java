package ai.yarmook.shipperfinder.core.notification.service;

import ai.yarmook.shipperfinder.core.notification.model.ExpoPushMessage;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PushNotificationService {

    private final String EXPO_PUSH_URL = "https://exp.host/--/api/v2/push/send";

    public void sendNotification(ExpoPushMessage message) {
        RestTemplate restTemplate = new RestTemplate();
        try {
            restTemplate.postForEntity(EXPO_PUSH_URL, message, String.class);
            System.out.println("Notification sent successfully.");
        } catch (Exception e) {
            System.err.println("Error sending notification: " + e.getMessage());
        }
    }
}
