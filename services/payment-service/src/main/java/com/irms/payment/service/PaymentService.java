package com.irms.payment.service;

import com.irms.payment.domain.Payment;
import com.irms.payment.domain.PaymentStatus;
import com.irms.payment.dto.PaymentRequestDTO;
import com.irms.payment.dto.PaymentResponseDTO;
import com.irms.payment.infrastructure.client.OrderServiceClient;
import com.irms.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@SuppressWarnings("null")
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderServiceClient orderServiceClient;

    @Transactional
    public PaymentResponseDTO createPayment(PaymentRequestDTO request) {
        // Here we would typically verify the amount with the OrderService
        // For MVP, we trust the amount and create a PENDING payment
        
        // Prevent duplicate payments for the same order
        paymentRepository.findByOrderId(request.getOrderId()).ifPresent(p -> {
            if (p.getStatus() == PaymentStatus.COMPLETED) {
                throw new RuntimeException("Order has already been paid.");
            }
        });

        Payment payment = Payment.builder()
                .orderId(request.getOrderId())
                .amount(request.getAmount())
                .method(request.getMethod())
                .status(PaymentStatus.PENDING)
                .build();

        return toDto(paymentRepository.save(payment));
    }

    @Transactional
    public PaymentResponseDTO processPayment(UUID paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        if (payment.getStatus() == PaymentStatus.COMPLETED) {
            throw new RuntimeException("Payment is already completed");
        }

        // Mock 3rd party processing
        payment.setStatus(PaymentStatus.COMPLETED);
        payment.setTransactionId("TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());

        paymentRepository.save(payment);

        // Notify Order Service
        orderServiceClient.updateOrderStatusToCompleted(payment.getOrderId());

        return toDto(payment);
    }

    public PaymentResponseDTO getPayment(UUID paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
        return toDto(payment);
    }

    private PaymentResponseDTO toDto(Payment payment) {
        return PaymentResponseDTO.builder()
                .id(payment.getId())
                .orderId(payment.getOrderId())
                .amount(payment.getAmount())
                .method(payment.getMethod())
                .status(payment.getStatus())
                .transactionId(payment.getTransactionId())
                .createdAt(payment.getCreatedAt())
                .updatedAt(payment.getUpdatedAt())
                .build();
    }
}
