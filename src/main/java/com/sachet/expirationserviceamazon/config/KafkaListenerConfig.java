package com.sachet.expirationserviceamazon.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sachet.expirationserviceamazon.modal.OrderCreatedEventModal;
import com.sachet.expirationserviceamazon.modal.OrderExpiredEvent;
import com.sachet.expirationserviceamazon.publisher.OrderExpiredEventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisKeyExpiredEvent;
import org.springframework.stereotype.Component;

@Component
public class KafkaListenerConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaListenerConfig.class);
    private final OrderExpiredEventPublisher orderExpiredEventPublisher;

    public KafkaListenerConfig(OrderExpiredEventPublisher orderExpiredEventPublisher) {
        this.orderExpiredEventPublisher = orderExpiredEventPublisher;
    }

    @EventListener
    public void expireOrder(RedisKeyExpiredEvent<OrderCreatedEventModal> expiredEvent)
    throws JsonProcessingException {
        OrderCreatedEventModal orderCreatedEventModal = (OrderCreatedEventModal) expiredEvent.getValue();
        LOGGER.info("Expiring: {}", orderCreatedEventModal);
        assert orderCreatedEventModal != null;
        OrderExpiredEvent orderExpiredEvent = new OrderExpiredEvent(orderCreatedEventModal.getOrderId(),
                orderCreatedEventModal.getItemId(), orderCreatedEventModal.getOrderPrice());
        orderExpiredEventPublisher.orderExpireEvent(orderExpiredEvent);
    }
}
