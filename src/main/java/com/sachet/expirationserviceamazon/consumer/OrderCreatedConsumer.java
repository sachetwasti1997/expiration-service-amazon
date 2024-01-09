package com.sachet.expirationserviceamazon.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sachet.expirationserviceamazon.modal.OrderCreatedEventModal;
import com.sachet.expirationserviceamazon.repository.ExpirationRepository;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.AcknowledgingMessageListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Component
public class OrderCreatedConsumer implements AcknowledgingMessageListener<String, String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderCreatedConsumer.class);

    private final ObjectMapper objectMapper;
    private final ExpirationRepository expirationRepository;

    public OrderCreatedConsumer(ObjectMapper objectMapper, ExpirationRepository expirationRepository) {
        this.objectMapper = objectMapper;
        this.expirationRepository = expirationRepository;
    }

    @KafkaListener(
            topics = "${spring.kafka.ordercreatedtopic}",
            containerFactory = "kafkaOrderCreatedListenerContainerFactory",
            groupId = "${spring.kafka.orderexpireconsumers.group-id}"
    )
    @Override
    public void onMessage(ConsumerRecord<String, String> consumerRecord, Acknowledgment acknowledgment) {
        try {
            var order = objectMapper.readValue(consumerRecord.value(), OrderCreatedEventModal.class);
            SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", new Locale("us"));
            var expDate = sdf.parse(order.getExpiresAt());
            var date = new Date();
            var expMillis = expDate.getTime() - date.getTime();
            LOGGER.info("The order: {} will expire after: {}", order, expMillis);
            order.setExpTime(expMillis);
            order = expirationRepository.save(order);
            LOGGER.info("Saved to redis: {}", order);
            assert acknowledgment != null;
            acknowledgment.acknowledge();
        } catch (JsonProcessingException | ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
