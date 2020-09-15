package passengers.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.cloudfoundry.com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import passengers.config.KafkaProperties;
import passengers.domain.NotificationRepo;
import passengers.repository.NotificationRepoRepository;
import passengers.service.dto.NotificationDTO;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class PassengerKafkaConsumer {

    private static final Logger logger = LoggerFactory.getLogger(PassengerKafkaConsumer.class);
    private static final String TOPIC_NOTIFICATION_SET = "notification_set";

    private final AtomicBoolean closed = new AtomicBoolean(false);
    private final KafkaProperties kafkaProperties;
    private final NotificationRepoRepository notificationRepoRepository;

    private KafkaConsumer<String, String> consumer;
    private ExecutorService executorService = Executors.newCachedThreadPool();

    public PassengerKafkaConsumer(KafkaProperties kafkaProperties, NotificationRepoRepository notificationRepoRepository) {
        this.kafkaProperties = kafkaProperties;
        this.notificationRepoRepository = notificationRepoRepository;
    }

    @PostConstruct
    public void start() {
        this.consumer = new KafkaConsumer<>(kafkaProperties.getConsumerProps());
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
        consumer.subscribe(Collections.singletonList(TOPIC_NOTIFICATION_SET));
        logger.debug("Passenger kafka consumer started.");

        executorService.execute(() -> {
            try {
                while (!closed.get()) {
                    ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(3));
                    for (ConsumerRecord<String, String> record : records) {
                        logger.info("Consumed message in {} : {}", TOPIC_NOTIFICATION_SET, record.value());

                        ObjectMapper objectMapper = new ObjectMapper();
                        NotificationDTO notificationDTO = objectMapper.readValue(record.value(), NotificationDTO.class);
                        // save notification in database for passengers to view
                        NotificationRepo notification = new NotificationRepo(notificationDTO.getName(), notificationDTO.getMessage());
                        notificationRepoRepository.save(notification);
                    }
                }
                consumer.commitSync();
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            } finally {
                logger.debug("Kafka consumer close");
                consumer.close();
            }
        });
    }


    public void shutdown() {
        logger.info("Shutdown Kafka consumer");
        closed.set(true);
        consumer.wakeup();
    }
}
