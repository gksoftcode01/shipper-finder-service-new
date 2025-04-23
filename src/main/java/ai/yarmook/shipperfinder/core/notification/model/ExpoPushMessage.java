package ai.yarmook.shipperfinder.core.notification.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.niamedtech.expo.exposerversdk.request.PushNotification;

public class ExpoPushMessage {

    @JsonProperty("to")
    private String to;

    @JsonProperty("userId")
    private String userId;

    @JsonProperty("title")
    private String title;

    @JsonProperty("body")
    private String body;

    @JsonProperty("data")
    private String data;

    @JsonProperty("priority")
    private PushNotification.Priority priority = PushNotification.Priority.NORMAL;

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public PushNotification.Priority getPriority() {
        return priority;
    }

    public void setPriority(PushNotification.Priority priority) {
        this.priority = priority;
    }

    public ExpoPushMessage userId(String userId) {
        this.userId = userId;
        return this;
    }

    public ExpoPushMessage to(String to) {
        this.to = to;
        return this;
    }

    public ExpoPushMessage title(String title) {
        this.title = title;
        return this;
    }

    public ExpoPushMessage body(String body) {
        this.body = body;
        return this;
    }

    public ExpoPushMessage data(String data) {
        this.data = data;
        return this;
    }

    public ExpoPushMessage priority(PushNotification.Priority priority) {
        this.priority = priority;
        return this;
    }
}
