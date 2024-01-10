package com.sachet.expirationserviceamazon.modal;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.util.concurrent.TimeUnit;

@RedisHash("order")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
@ToString
public class OrderCreatedEventModal {
    @Id
    private String orderId;
    private String status;
    @TimeToLive(unit = TimeUnit.MILLISECONDS)
    private Long expTime;
    private String expiresAt;
    private String itemId;
    private Integer orderQuantity;
    private Double orderPrice;
}
