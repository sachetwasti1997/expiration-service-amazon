package com.sachet.expirationserviceamazon.modal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PaymentStatus {
    private String orderId;
    private String paymentId;
    private String paymentStatus;
}
