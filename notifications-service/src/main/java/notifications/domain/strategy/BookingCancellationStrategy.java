package notifications.domain.strategy;

import notifications.domain.Notification;
import notifications.domain.enumeration.ENotificationState;
import notifications.domain.enumeration.ENotificationType;
import notifications.web.converters.BookingData;

public class BookingCancellationStrategy implements NotificationStrategy {

    private BookingData subjectToChange;

    //TODO Check if subject to change casting to local object is valid
    public BookingCancellationStrategy(Object subjectToChange) {
        this.subjectToChange = (BookingData) subjectToChange;
    }

    @Override
    public Notification createNotification() {

        //TODO add description based on booking number
        String description = "";

        return new Notification(ENotificationType.BOOKING_CANCELLED, ENotificationState.PENDING, description);

    }
}
