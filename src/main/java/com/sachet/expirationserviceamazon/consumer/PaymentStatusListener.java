package com.sachet.expirationserviceamazon.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sachet.expirationserviceamazon.modal.PaymentStatus;
import com.sachet.expirationserviceamazon.repository.ExpirationRepository;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.AcknowledgingMessageListener;
import org.springframework.kafka.support.Acknowledgment;

@Configuration
public class PaymentStatusListener implements AcknowledgingMessageListener<String, String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentStatusListener.class);

    private final ObjectMapper objectMapper;
    private final ExpirationRepository expirationRepository;

    public PaymentStatusListener(ObjectMapper objectMapper, ExpirationRepository expirationRepository) {
        this.objectMapper = objectMapper;
        this.expirationRepository = expirationRepository;
    }

    @KafkaListener(
            topics = "${spring.kafka.paymentTopic}",
            containerFactory = "kafkaPaymentStatusListenerContainerFactory",
            groupId = "${spring.kafka.orderexpireconsumers.group-id}"
    )
    @Override
    public void onMessage(ConsumerRecord<String, String> data, Acknowledgment acknowledgment) {
        LOGGER.info("Consuming Payment Status {}", data);
        try {
            PaymentStatus paymentStatus = objectMapper.readValue(data.value(), PaymentStatus.class);
            expirationRepository.deleteById(paymentStatus.getOrderId());
        }catch (JsonProcessingException ex) {
            LOGGER.error("Error while processing JSON {},",ex.getMessage());
        }
    }
}
