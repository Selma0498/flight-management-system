package notifications.service;

import notifications.config.KafkaProperties;
import notifications.domain.Notification;
import notifications.domain.enumeration.ENotificationType;
import notifications.service.dto.BookingDTO;
import notifications.service.dto.FlightDTO;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.cloudfoundry.com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * In charge of handling the events published by Flight and Booking microservices, upon updates or cancellation
 * of flights i.e. bookings. When subscribing for a single topic Kafka guarantees the correct order of messages,
 * same goes for a single partition consumption. However for multiple topics with a same consumer, Kafka assigns
 * that Consumer a partition for each topic and it guarantees a correct order of messages within each partition,
 * but not among partitions. In this case that means that theoretically messages for flight updates and flight
 * cancellation can be consumed in a wrong order, which would mean the service would proceed to informing the
 * passenger in the wrong order as well, leading to a faulty functionality. To avoid this a consumer per topic
 * approach is used.
 */
@Service
public class NotificationKafkaConsumer {

    private static final String TOPIC_BOOKING_UPDATED = "booking_updated";
    private static final String TOPIC_BOOKING_CANCELLED = "booking_cancelled";
    private static final String TOPIC_FLIGHT_UPDATED = "flight_updated";
    private static final String TOPIC_FLIGHT_CANCELLED = "flight_cancelled";

    private final Logger logger = LoggerFactory.getLogger(NotificationKafkaConsumer.class);
    private final AtomicBoolean closed = new AtomicBoolean(false);
    private final KafkaProperties kafkaProperties;
    private final NotificationKafkaProducer notificationKafkaProducer;

    private KafkaConsumer<String, String> bookingUpdateConsumer;
    private KafkaConsumer<String, String> bookingCancellationConsumer;
    private KafkaConsumer<String, String> flightUpdateConsumer;
    private KafkaConsumer<String, String> flightCancellationConsumer;
    private ExecutorService executorService = Executors.newCachedThreadPool();
    private String notificationMessage = "";

    public NotificationKafkaConsumer(KafkaProperties kafkaProperties, NotificationKafkaProducer notificationKafkaProducer) {
        this.kafkaProperties = kafkaProperties;
        this.notificationKafkaProducer = notificationKafkaProducer;
    }

    @PostConstruct
    public void start() {
        this.bookingUpdateConsumer = new KafkaConsumer<>(kafkaProperties.getConsumerProps());
        this.bookingCancellationConsumer = new KafkaConsumer<>(kafkaProperties.getConsumerProps());
        this.flightUpdateConsumer = new KafkaConsumer<>(kafkaProperties.getConsumerProps());
        this.flightCancellationConsumer = new KafkaConsumer<>(kafkaProperties.getConsumerProps());
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));

        bookingUpdateConsumer.subscribe(Collections.singletonList(TOPIC_BOOKING_UPDATED));
        bookingCancellationConsumer.subscribe(Collections.singletonList(TOPIC_BOOKING_CANCELLED));
        flightUpdateConsumer.subscribe(Collections.singletonList(TOPIC_FLIGHT_UPDATED));
        flightCancellationConsumer.subscribe(Collections.singletonList(TOPIC_FLIGHT_CANCELLED));

        logger.debug("Notification kafka consumers (flight update & cancellation and booking update & cancellation) started.");

        executorService.execute(() -> {
            try {
                while (!closed.get()) {
                    ConsumerRecords<String, String> bookingUpdateRecords = bookingUpdateConsumer.poll(Duration.ofSeconds(3));
                    ConsumerRecords<String, String> bookingCancellationRecords = bookingCancellationConsumer.poll(Duration.ofSeconds(3));
                    ConsumerRecords<String, String> flightUpdateRecords = flightUpdateConsumer.poll(Duration.ofSeconds(3));
                    ConsumerRecords<String, String> flightCancellationRecords = flightCancellationConsumer.poll(Duration.ofSeconds(3));
                    for (ConsumerRecord<String, String> record : bookingUpdateRecords) {
                        logger.debug("Consumed message in {} : {}", TOPIC_BOOKING_UPDATED, record.value());

                        ObjectMapper objectMapper = new ObjectMapper();
                        BookingDTO bookingDTO = objectMapper.readValue(record.value(), BookingDTO.class);
                        // send the notification for the passenger to subscribe for
                        this.notificationMessage = "This information is for a passenger with passenger id: " + bookingDTO.getPassengerId() + "" +
                            ". If this concerns you, please read on. " +
                            "Dear Sir/Madam, the booking with booking number: " + bookingDTO.getBookingNumber() +
                            " for flight with flight number: " + bookingDTO.getFlightNumber() +
                            " has been confirmed. Safe travels and best regards!";
                        notificationKafkaProducer.sendNotificationEvent(
                            new Notification(ENotificationType.BOOKING_CONFIRMED, notificationMessage)
                        );

                    }
                    for (ConsumerRecord<String, String> record : bookingCancellationRecords) {
                        logger.debug("Consumed message in {} : {}", TOPIC_BOOKING_CANCELLED, record.value());

                        ObjectMapper objectMapper = new ObjectMapper();
                        BookingDTO bookingDTO = objectMapper.readValue(record.value(), BookingDTO.class);
                        // send the notification for the passenger to subscribe for
                        this.notificationMessage = "This information is for a passenger with passenger id: " + bookingDTO.getPassengerId() + "" +
                            ". If this concerns you, please read on. " +
                            "Dear Sir/Madam, the booking with booking number: " + bookingDTO.getBookingNumber() +
                            " for flight with flight number: " + bookingDTO.getFlightNumber() + " has been cancelled. Best regards!";

                        notificationKafkaProducer.sendNotificationEvent(
                            new Notification(ENotificationType.BOOKING_CANCELLED, notificationMessage)
                        );
                    }
                    for (ConsumerRecord<String, String> record : flightUpdateRecords) {
                        logger.debug("Consumed message in {} : {}", TOPIC_FLIGHT_UPDATED, record.value());

                        ObjectMapper objectMapper = new ObjectMapper();
                        FlightDTO flightDTO = objectMapper.readValue(record.value(), FlightDTO.class);
                        // send the notification for the passenger to subscribe for
                        this.notificationMessage = "Dear Sir/Madam, the flight with flight number: " + flightDTO.getFlightNumber() +
                            " for origin: " + flightDTO.getOrigin() + " and destination: " + flightDTO.getDestination() +
                            " with departure date on: " + flightDTO.getDepartureDate() +
                            " has been updated. Safe travels and best regards!";

                        notificationKafkaProducer.sendNotificationEvent(
                            new Notification(ENotificationType.FLIGHT_UPDATED, notificationMessage)
                        );
                    }
                    for (ConsumerRecord<String, String> record : flightCancellationRecords) {
                        logger.debug("Consumed message in {} : {}", TOPIC_FLIGHT_CANCELLED, record.value());

                        ObjectMapper objectMapper = new ObjectMapper();
                        FlightDTO flightDTO = objectMapper.readValue(record.value(), FlightDTO.class);
                       // send the notification for the passenger to subscribe for
                        this.notificationMessage = "Dear Sir/Madam, the flight with flight number: " + flightDTO.getFlightNumber() +
                            " for origin: " + flightDTO.getOrigin() + " and destination: " + flightDTO.getDestination() +
                            " with departure date on: " + flightDTO.getDepartureDate() + " has been cancelled. Best regards!";

                        notificationKafkaProducer.sendNotificationEvent(
                            new Notification(ENotificationType.FLIGHT_CANCELLED, notificationMessage)
                        );
                    }
                }
                bookingUpdateConsumer.commitSync();
                bookingCancellationConsumer.commitSync();
                flightUpdateConsumer.commitSync();
                flightCancellationConsumer.commitSync();
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            } finally {
                logger.debug("Kafka consumers closing");
                bookingUpdateConsumer.close();
                bookingCancellationConsumer.close();
                flightUpdateConsumer.close();
                flightCancellationConsumer.close();
            }
        });
    }

    public void shutdown() {
        logger.debug("Shutdown Kafka consumer");
        closed.set(true);
        bookingUpdateConsumer.wakeup();
        bookingCancellationConsumer.wakeup();
        flightUpdateConsumer.wakeup();
        flightCancellationConsumer.wakeup();
    }


}

