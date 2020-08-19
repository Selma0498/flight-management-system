package notifications.domain.strategy;

import notifications.domain.Notification;
import notifications.domain.enumeration.ENotificationType;

public class FlightCancellationStrategy implements NotificationStrategy {

    private String notificationMesage = "";

    public FlightCancellationStrategy() {
        this.notificationMesage = "Notification type: " + ENotificationType.FLIGHT_CANCELLED.toString() +
            " Message: The flight has been cancelled.";
    }

    @Override
    public Notification getNotification() {
        return new Notification(ENotificationType.FLIGHT_CANCELLED, this.notificationMesage);
    }

}
