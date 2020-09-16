package bookings.service;

import bookings.config.KafkaProperties;
import bookings.domain.Booking;
import bookings.domain.enumeration.ETopicType;
import bookings.service.dto.BookingDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Service
public class BookingKafkaProducer {

    private static final Logger logger = LoggerFactory.getLogger(BookingKafkaProducer.class);
    private static final String TOPIC_BOOKING_UPDATED = "booking_updated";
    private static final String TOPIC_BOOKING_CANCELLED = "booking_cancelled";

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final KafkaProperties kafkaProperties;
    private KafkaProducer<String, String> producer;

    public BookingKafkaProducer(KafkaProperties kafkaProperties) {
        this.kafkaProperties = kafkaProperties;
    }

    @PostConstruct
    public void initialize() {
        this.producer = new KafkaProducer<>(kafkaProperties.getProducerProps());
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
        logger.debug("Booking kafka producer initialized.");
    }

    public void sendBookingEvent(Booking booking, ETopicType topicType) {
        try{
            BookingDTO bookingDTO = new BookingDTO(booking);
            String message = objectMapper.writeValueAsString(bookingDTO);
            ProducerRecord<String, String> event = null;

            switch (topicType) {
                case SET:
                case UPDATED:
                    event = new ProducerRecord<>(TOPIC_BOOKING_UPDATED, message);
                    logger.debug("Produced an event for topic {} : {}", TOPIC_BOOKING_UPDATED, event.value());
                    break;
                case CANCELLED:
                    event = new ProducerRecord<>(TOPIC_BOOKING_CANCELLED, message);
                    logger.debug("Produced an event for topic {} : {}", TOPIC_BOOKING_CANCELLED, event.value());
                    break;
            }

            producer.send(event);
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
        }
    }

    @PreDestroy
    private void shutdown() {
        logger.debug("Booking kafka producer shutting down");
        producer.close();
    }
}

