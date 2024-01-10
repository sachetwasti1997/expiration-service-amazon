package com.sachet.expirationserviceamazon.modal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class OrderExpiredEvent {
    private String orderId;
    private String itemId;
    private Double orderPrice;
}
