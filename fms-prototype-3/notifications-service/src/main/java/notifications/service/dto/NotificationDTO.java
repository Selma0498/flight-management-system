package notifications.service.dto;

import notifications.domain.Notification;

public class NotificationDTO {

    private String name;
    private String message;

    public NotificationDTO(Notification notification) {
        this.name = notification.getNotificationType().name();
        this.message = notification.getDescription();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
