package notifications.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import notifications.config.KafkaProperties;
import notifications.domain.Notification;
import notifications.service.dto.NotificationDTO;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Service
public class NotificationKafkaProducer {

    private static Logger logger = LoggerFactory.getLogger(NotificationKafkaProducer.class);
    private static final String TOPIC_NOTIFICATION_SET = "notification_set";

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final KafkaProperties kafkaProperties;
    private KafkaProducer<String, String> producer;

    public NotificationKafkaProducer(KafkaProperties kafkaProperties) {
        this.kafkaProperties = kafkaProperties;
    }

    @PostConstruct
    public void initialize() {
        this.producer = new KafkaProducer<>(kafkaProperties.getProducerProps());
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
        logger.debug("Notifications kafka producer initialized.");
    }

    public void sendNotificationEvent(Notification notification) {
        try{
            NotificationDTO notificationDTO = new NotificationDTO(notification);
            String message = objectMapper.writeValueAsString(notificationDTO);
            ProducerRecord<String, String> event = new ProducerRecord<>(TOPIC_NOTIFICATION_SET, message);
            logger.debug("Produced an event for topic {} : {}", TOPIC_NOTIFICATION_SET, event.value());
            producer.send(event);
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
        }
    }

    @PreDestroy
    private void shutdown() {
        logger.debug("Notifications kafka producer shutting down");
        producer.close();
    }

}
