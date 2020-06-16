package notifications.domain;

import notifications.domain.enumeration.ENotificationState;
import notifications.domain.enumeration.ENotificationType;

//TODO Could potentially delete pending/sent states &|| make notifications storable and available for sending to multiple passengers in the same time
//TODO for flight updates the list of recipients makes sense!
public class Notification {

    private ENotificationType notificationType;
    private ENotificationState notificationState;
    private String description;

    public Notification(ENotificationType notificationType, ENotificationState notificationState, String description) {
        this.notificationType = notificationType;
        this.notificationState = notificationState;
        this.description = description;
    }

    public ENotificationType getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(ENotificationType notificationType) {
        this.notificationType = notificationType;
    }

    public ENotificationState getNotificationState() {
        return notificationState;
    }

    public void setNotificationState(ENotificationState notificationState) {
        this.notificationState = notificationState;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
