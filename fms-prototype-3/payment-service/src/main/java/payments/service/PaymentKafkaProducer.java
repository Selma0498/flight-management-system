package payments.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import payments.config.KafkaProperties;
import payments.domain.Payment;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Service
public class PaymentKafkaProducer {

    private static final Logger logger = LoggerFactory.getLogger(PaymentKafkaProducer.class);
    private static final String TOPIC_PAYMENT_SET = "topic_payment_set";

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final KafkaProperties kafkaProperties;
    private KafkaProducer<String, String> producer;

    public PaymentKafkaProducer(KafkaProperties kafkaProperties) {
        this.kafkaProperties = kafkaProperties;
    }

    @PostConstruct
    public void initialize() {
        this.producer = new KafkaProducer<>(kafkaProperties.getProducerProps());
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
        logger.debug("Payment kafka producer intialized");
    }

    public void sendPaymentSuccess(Payment payment) {
        try{
            String message = objectMapper.writeValueAsString(payment);
            ProducerRecord<String, String> event = new ProducerRecord<>(TOPIC_PAYMENT_SET, message);
            producer.send(event);
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
        }
    }

    @PreDestroy
    private void shutdown() {
        logger.debug("Payment kafka producer shutting down");
        producer.close();
    }
}
