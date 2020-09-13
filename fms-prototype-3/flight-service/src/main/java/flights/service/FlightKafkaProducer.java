package flights.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import flights.config.KafkaProperties;
import flights.domain.Flight;
import flights.domain.enumeration.ETopicType;
import flights.service.dto.FlightDTO;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Service
public class FlightKafkaProducer {

    private static final Logger logger = LoggerFactory.getLogger(FlightKafkaProducer.class);
    private static final String TOPIC_FLIGHT_SET = "flight_set";
    private static final String TOPIC_FLIGHT_UPDATED = "flight_updated";
    private static final String TOPIC_FLIGHT_CANCELLED = "flight_cancelled";

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final KafkaProperties kafkaProperties;
    private KafkaProducer<String, String> producer;

    public FlightKafkaProducer(KafkaProperties kafkaProperties) {
        this.kafkaProperties = kafkaProperties;
    }

    @PostConstruct
    public void initialize() {
        this.producer = new KafkaProducer<>(kafkaProperties.getProducerProps());
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
        logger.debug("Flight kafka producer initialized.");
    }

    public void sendFlightEvent(Flight flight, ETopicType topicType) {
        try{
            FlightDTO flightDTO = new FlightDTO(flight);
            String message = objectMapper.writeValueAsString(flightDTO);
            ProducerRecord<String, String> event = null;

            switch (topicType) {
                case SET: event = new ProducerRecord<>(TOPIC_FLIGHT_SET, message);
                    break;
                case UPDATED: event = new ProducerRecord<>(TOPIC_FLIGHT_UPDATED, message);
                    break;
                case CANCELLED: event = new ProducerRecord<>(TOPIC_FLIGHT_CANCELLED, message);
                    break;
            }

            producer.send(event);
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
        }
    }

    @PreDestroy
    private void shutdown() {
        logger.debug("Flight kafka producer shutting down");
        producer.close();
    }
}

