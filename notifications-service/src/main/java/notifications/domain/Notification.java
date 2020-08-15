package notifications.domain;

import notifications.domain.enumeration.ENotificationType;

public class Notification {

    private ENotificationType notificationType;
    private String description;

    public Notification(ENotificationType notificationType, String description) {
        this.notificationType = notificationType;
        this.description = description;
    }

    public ENotificationType getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(ENotificationType notificationType) {
        this.notificationType = notificationType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
