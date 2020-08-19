package notifications.web.rest;

import notifications.domain.Notification;
import notifications.domain.NotificationContext;
import notifications.domain.enumeration.ENotificationType;
import notifications.domain.strategy.BookingCancellationStrategy;
import notifications.domain.strategy.BookingConfirmationStrategy;
import notifications.domain.strategy.FlightCancellationStrategy;
import notifications.domain.strategy.FlightUpdateStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;

/**
 * REST controller for managing {@Link notifications.domain.Notification}.
 */
@RestController
@RequestMapping("/api")
public class NotificationResource {

    private final Logger log = LoggerFactory.getLogger(NotificationResource.class);

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    /**
     * {@code POST /notify} : Create a notification based on notification type and subject provided.
     *
     * @param notificationType notification type.
     * @return the {@Link ResponseEntity} with status {@code 200 (OK)} and Notification to be forwarded to user
     * or with status {@code 400 (Bad Request)} if the arguments provided are invalid,
     * or with status {@code 500 (Internal Server Error)} if the notification could not be created.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @GetMapping("/notify")
    public ResponseEntity<Notification> createNotification(@RequestParam ENotificationType notificationType) throws URISyntaxException {

        if(notificationType == null) {
            throw new IllegalArgumentException("Notification type must be defined.");
        }

        log.debug("REST request to create notification with type : {}", notificationType);

        // decide on notification creation strategy based on the notification type
        NotificationContext context;

        switch(notificationType) {
            case FLIGHT_UPDATED:
                context = new NotificationContext(new FlightUpdateStrategy());
                break;
            case FLIGHT_CANCELLED:
                context = new NotificationContext(new FlightCancellationStrategy());
                break;
            case BOOKING_CANCELLED:
                context = new NotificationContext(new BookingCancellationStrategy());
                break;
            case BOOKING_CONFIRMED:
                context = new NotificationContext(new BookingConfirmationStrategy());
                break;
                default:
                    throw new IllegalArgumentException("Unknown notification type requested.");
        }

        Notification result = context.getNotification();

        return ResponseEntity.ok()
            .body(result);

    }
}
