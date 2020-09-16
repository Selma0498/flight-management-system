package bookings.service;

import bookings.config.KafkaProperties;
import bookings.repository.BookingRepository;
import bookings.service.dto.LuggageDTO;
import bookings.service.dto.PaymentDTO;
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

@Service
public class BookingKafkaConsumer {

    private static final Logger logger = LoggerFactory.getLogger(BookingKafkaConsumer.class);
    private static final String TOPIC_PAYMENT_SET = "payment_set";
    private static final String TOPIC_LUGGAGE_SET = "luggage_set";

    private final AtomicBoolean closed = new AtomicBoolean(false);
    private final KafkaProperties kafkaProperties;
    private final BookingRepository bookingRepository;

    private KafkaConsumer<String, String> paymentConsumer;
    private KafkaConsumer<String, String> luggageConsumer;
    private ExecutorService executorService = Executors.newCachedThreadPool();

    public BookingKafkaConsumer(KafkaProperties kafkaProperties, BookingRepository bookingRepository) {
        this.kafkaProperties = kafkaProperties;
        this.bookingRepository = bookingRepository;
    }

    @PostConstruct
    public void start() {
        this.paymentConsumer = new KafkaConsumer<>(kafkaProperties.getConsumerProps());
        this.luggageConsumer = new KafkaConsumer<>(kafkaProperties.getConsumerProps());
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
        paymentConsumer.subscribe(Collections.singletonList(TOPIC_PAYMENT_SET));
        luggageConsumer.subscribe(Collections.singletonList(TOPIC_LUGGAGE_SET));
        logger.debug("Booking kafka consumers (payment & luggage) started.");

        executorService.execute(() -> {
            try {
                while (!closed.get()) {
                    ConsumerRecords<String, String> paymentRecords = paymentConsumer.poll(Duration.ofSeconds(3));
                    ConsumerRecords<String, String> luggageRecords = luggageConsumer.poll(Duration.ofSeconds(3));

                    for (ConsumerRecord<String, String> record : paymentRecords) {
                        logger.debug("Consumed message in {} : {}", TOPIC_PAYMENT_SET, record.value());

                        ObjectMapper objectMapper = new ObjectMapper();
                        PaymentDTO paymentDTO = objectMapper.readValue(record.value(), PaymentDTO.class);
                        if(bookingRepository.findBookingByBookingNumber(Integer.valueOf(paymentDTO.getBookingNumber())) == null) {
                            throw new Exception("Payment successful for a booking that does not exist.");
                        }
                    }
                    for (ConsumerRecord<String, String> record : luggageRecords) {
                        logger.debug("Consumed message in {} : {}", TOPIC_LUGGAGE_SET, record.value());

                        ObjectMapper objectMapper = new ObjectMapper();
                        LuggageDTO luggageDTO = objectMapper.readValue(record.value(), LuggageDTO.class);
                        if(bookingRepository.findBookingByBookingNumber(Integer.valueOf(luggageDTO.getBookingNumber())) == null) {
                            throw new Exception("Luggage successful for a booking that does not exist.");
                        }

                    }
                }
                paymentConsumer.commitSync();
                luggageConsumer.commitSync();
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            } finally {
                logger.debug("Kafka consumer close");
                paymentConsumer.close();
                luggageConsumer.close();
            }
        });
    }


    private void shutdown() {
        logger.debug("Shutdown Kafka consumer");
        closed.set(true);
        paymentConsumer.wakeup();
        luggageConsumer.wakeup();
    }

}
