package notifications.domain.strategy;

import notifications.domain.Notification;

public interface NotificationStrategy {

    Notification createNotification();
}
