package payments.web.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.context.event.EventListener;
import payments.config.KafkaProperties;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import payments.domain.CreditCard;
import payments.domain.Payment;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/api/payments-kafka")
public class PaymentsKafkaResource {

    private final Logger log = LoggerFactory.getLogger(PaymentsKafkaResource.class);

    private static final String TOPIC = "topic_payment";
    private static final String PAYMENT_SET_TOPIC = "payment_set";
    private static final String PROCESS_PAYMENT_TOPIC = "process_payment";

    private final KafkaProperties kafkaProperties;
    private KafkaProducer<String, String> producer;
    private ExecutorService sseExecutorService = Executors.newCachedThreadPool();

    public PaymentsKafkaResource(KafkaProperties kafkaProperties) {
        this.kafkaProperties = kafkaProperties;
        this.producer = new KafkaProducer<>(kafkaProperties.getProducerProps());
    }

    @PostMapping("/publish")
    public PublishResult publish(@RequestParam String message, @RequestParam(required = false) String key) throws ExecutionException, InterruptedException {
        log.debug("REST request to send to Kafka topic {} with key {} the message : {}", TOPIC, key, message);
        log.info("Producing message to {} : {}", TOPIC, message);
        RecordMetadata metadata = producer.send(new ProducerRecord<>(TOPIC, key, message)).get();
        return new PublishResult(metadata.topic(), metadata.partition(), metadata.offset(), Instant.ofEpochMilli(metadata.timestamp()));
    }

    @GetMapping("/consume")
    public SseEmitter consume(@RequestParam("topic_payment") List<String> topics, @RequestParam Map<String, String> consumerParams) {
        log.debug("REST request to consume records from Kafka topics {}", topics);
        Map<String, Object> consumerProps = kafkaProperties.getConsumerProps();
        consumerProps.putAll(consumerParams);
        consumerProps.remove("topic_payment");

        SseEmitter emitter = new SseEmitter(0L);
        sseExecutorService.execute(() -> {
            KafkaConsumer<String, String> consumer = new KafkaConsumer<>(consumerProps);
            emitter.onCompletion(consumer::close);
            consumer.subscribe(topics);

            boolean exitLoop = false;
            while(!exitLoop) {
                try {
                    ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(5));
                    for (ConsumerRecord<String, String> record : records) {
                        emitter.send(record.value());
                    }
                    emitter.send(SseEmitter.event().comment(""));
                } catch (Exception ex) {
                    log.trace("Complete with error {}", ex.getMessage(), ex);
                    emitter.completeWithError(ex);
                    exitLoop = true;
                }
            }
            consumer.close();
            emitter.complete();
        });
        return emitter;
    }

    private static class PublishResult {
        public final String topic;
        public final int partition;
        public final long offset;
        public final Instant timestamp;

        private PublishResult(String topic, int partition, long offset, Instant timestamp) {
            this.topic = topic;
            this.partition = partition;
            this.offset = offset;
            this.timestamp = timestamp;
        }
    }
}
