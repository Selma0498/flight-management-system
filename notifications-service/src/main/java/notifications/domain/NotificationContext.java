package notifications.domain;

import notifications.domain.strategy.NotificationStrategy;

public class NotificationContext {

    private NotificationStrategy strategy;

    public NotificationContext(NotificationStrategy strategy) {
        this.strategy = strategy;
    }

    public Notification createNotification() {
        return strategy.createNotification();
    }

}
