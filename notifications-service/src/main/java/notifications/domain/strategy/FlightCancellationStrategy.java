package notifications.domain.strategy;

import notifications.domain.Notification;
import notifications.domain.enumeration.ENotificationState;
import notifications.domain.enumeration.ENotificationType;
import notifications.web.converters.FlightData;

public class FlightCancellationStrategy implements NotificationStrategy {

    private FlightData subjectToChange;

    //TODO Check if subject to change casting to local object is valid


    public FlightCancellationStrategy(FlightData subjectToChange) {
        this.subjectToChange = subjectToChange;
    }

    @Override
    public Notification createNotification() {

        //TODO add description based on flight number
        String description = "";

        return new Notification(ENotificationType.FLIGHT_CANCELLED, ENotificationState.PENDING, description);
    }
}
