package com.irms.payment.controller;

import com.irms.payment.dto.PaymentRequestDTO;
import com.irms.payment.dto.PaymentResponseDTO;
import com.irms.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentResponseDTO> createPayment(@RequestBody PaymentRequestDTO request) {
        return new ResponseEntity<>(paymentService.createPayment(request), HttpStatus.CREATED);
    }

    @PostMapping("/{paymentId}/process")
    public ResponseEntity<PaymentResponseDTO> processPayment(@PathVariable UUID paymentId) {
        return ResponseEntity.ok(paymentService.processPayment(paymentId));
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentResponseDTO> getPayment(@PathVariable UUID paymentId) {
        return ResponseEntity.ok(paymentService.getPayment(paymentId));
    }
}
