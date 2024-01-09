package com.sachet.expirationserviceamazon.publisher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sachet.expirationserviceamazon.modal.OrderExpiredEvent;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class OrderExpiredEventPublisher {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderExpiredEventPublisher.class);
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final String topicName;

    public OrderExpiredEventPublisher(KafkaTemplate<String, String> kafkaTemplate,
                                      ObjectMapper objectMapper,
                                      @Value("${spring.kafka.expireordertopic}") String topicName) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
        this.topicName = topicName;
    }

    public CompletableFuture<SendResult<String, String>> orderExpireEvent(
            OrderExpiredEvent order
    ) throws JsonProcessingException {
        LOGGER.info("Order expired, {}", order);
        String key = order.getOrderId();
        String value = objectMapper.writeValueAsString(order);
        ProducerRecord<String, String> producerRecord = new ProducerRecord<>(topicName, key, value);
        return kafkaTemplate.send(producerRecord)
                .whenComplete(((result, throwable) -> {
                    if (throwable != null) {
                        handleFailure(key, value, throwable.getMessage());
                    } else {
                        handleSuccess(key, value, result);
                    }
                }));
    }

    private void handleSuccess(String key, String value, SendResult<String, String> result) {
        LOGGER.info("Successfully sent the event to kafka, with KEY: {}, VALUE: {}",
                key, result.getProducerRecord().value());
    }

    private void handleFailure(String key, String value, String message) {
        LOGGER.error("Unable to send message to Kafka, failed: {}", message);
    }
}
