package com.data.controller;

import com.data.dto.request.PaymentRequestDTO;
import com.data.dto.response.PaymentResponseDTO;
import com.data.service.PaymentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentController {
    PaymentService paymentService;

    @PostMapping("/create")
    public ResponseEntity<PaymentResponseDTO> createPayment(@RequestBody PaymentRequestDTO dto) {
        return ResponseEntity.ok(paymentService.createPayment(dto));
    }

    @PostMapping("/callback")
    public ResponseEntity<PaymentResponseDTO> paymentCallback(
            @RequestParam String transactionId,
            @RequestParam boolean success) {
        return ResponseEntity.ok(paymentService.handlePaymentCallback(transactionId, success));
    }

    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<PaymentResponseDTO> getPaymentByBooking(@PathVariable Long bookingId) {
        return ResponseEntity.ok(paymentService.getPaymentByBooking(bookingId));
    }

    @PostMapping("/{bookingId}/refund")
    public ResponseEntity<PaymentResponseDTO> refundPayment(
            @PathVariable Long bookingId,
            @RequestParam String reason) {
        return ResponseEntity.ok(paymentService.refundPayment(bookingId, reason));
    }

    @GetMapping
    public ResponseEntity<List<PaymentResponseDTO>> getAllPayments() {
        return ResponseEntity.ok(paymentService.getAllPayments());
    }
}
