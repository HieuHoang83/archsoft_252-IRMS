package com.irms.payment.dto;

import com.irms.payment.domain.PaymentMethod;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class PaymentRequestDTO {
    private UUID orderId;
    private PaymentMethod method;
    private BigDecimal amount; // Typically provided by frontend but verified against order-service
}
