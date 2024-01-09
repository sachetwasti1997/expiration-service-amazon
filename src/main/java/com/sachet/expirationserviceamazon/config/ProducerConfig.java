package com.sachet.expirationserviceamazon.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.stereotype.Component;

@Component
public class ProducerConfig {

    private final String topicName;

    public ProducerConfig(@Value("${spring.kafka.expireordertopic}") String topicName) {
        this.topicName = topicName;
    }

    @Bean
    public NewTopic orderExpiredTopic() {
        return TopicBuilder
                .name(topicName)
                .partitions(1)
                .replicas(1)
                .build();
    }
}
