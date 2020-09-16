package luggage.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import luggage.config.KafkaProperties;
import luggage.domain.Luggage;
import luggage.service.dto.LuggageDTO;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Service
public class LuggageKafkaProducer {

    private static Logger logger = LoggerFactory.getLogger(LuggageKafkaProducer.class);
    private static final String TOPIC_LUGGAGE_SET = "luggage_set";

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final KafkaProperties kafkaProperties;
    private KafkaProducer<String, String> producer;

    public LuggageKafkaProducer(KafkaProperties kafkaProperties) {
        this.kafkaProperties = kafkaProperties;
    }

    @PostConstruct
    public void initialize() {
        this.producer = new KafkaProducer<>(kafkaProperties.getProducerProps());
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
        logger.debug("Luggage kafka producer initialized.");
    }

    public void sendLuggageEvent(Luggage luggage) {
        try{
            LuggageDTO luggageDTO = new LuggageDTO(luggage);
            String message = objectMapper.writeValueAsString(luggageDTO);
            ProducerRecord<String, String> event = new ProducerRecord<>(TOPIC_LUGGAGE_SET, message);
            logger.debug("Produced an event for topic {} : {}", TOPIC_LUGGAGE_SET, event.value());
            producer.send(event);
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
        }
    }

    @PreDestroy
    private void shutdown() {
        logger.debug("Luggage kafka producer shutting down");
        producer.close();
    }
}
