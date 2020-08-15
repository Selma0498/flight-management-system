package notifications.domain.strategy;

import notifications.domain.Notification;
import notifications.domain.enumeration.ENotificationType;

public class BookingConfirmationStrategy implements NotificationStrategy {

    private String notificationMesage = "";

    public BookingConfirmationStrategy() {

        this.notificationMesage =  "Notification type: " + ENotificationType.BOOKING_CONFIRMED.toString() +
            " Message: The flight has been booked successfully. You can see your booking any time under the booking overview section. Safe travels! :)";

    }

    @Override
    public Notification getNotification() {
        return new Notification(ENotificationType.BOOKING_CONFIRMED, this.notificationMesage);
    }
}
