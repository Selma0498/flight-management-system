package notifications.domain.strategy;

import notifications.domain.Notification;
import notifications.domain.enumeration.ENotificationType;

public class FlightUpdateStrategy implements NotificationStrategy {

    private String notificationMesage = "";

    public FlightUpdateStrategy() {
        this.notificationMesage = "Notification type: " + ENotificationType.FLIGHT_UPDATED.toString() +
            " Message: The information for the flight has been updated. Please check for the new information in the Flight overview.";;
    }

    @Override
    public Notification getNotification() {
        return new Notification(ENotificationType.FLIGHT_UPDATED, this.notificationMesage);
    }

}
