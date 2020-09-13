package notifications.domain.strategy;

import notifications.domain.Notification;
import notifications.domain.enumeration.ENotificationType;

public class BookingCancellationStrategy implements NotificationStrategy {

    private String notificationMesage = "";

    public BookingCancellationStrategy() {
        this.notificationMesage = "Notification type: " + ENotificationType.BOOKING_CANCELLED.toString() +
            " Message: The booking has been successfully cancelled. ";
    }

    @Override
    public Notification getNotification() {
        return new Notification(ENotificationType.BOOKING_CANCELLED, this.notificationMesage);
    }
}
