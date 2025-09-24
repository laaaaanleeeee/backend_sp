package com.data.dto.response;

import com.data.entity.Payment;
import com.data.enums.PaymentMethod;
import com.data.enums.PaymentStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentResponseDTO {
     Long id;
     Long bookingId;
     Double amount;
     PaymentMethod method;
     PaymentStatus status;
     String transactionId;
     LocalDateTime paymentTime;

    public PaymentResponseDTO(Payment payment) {
        this.id = payment.getId();
        this.bookingId = payment.getBooking().getId();
        this.amount = payment.getAmount();
        this.method = payment.getPaymentMethod();
        this.status = payment.getPaymentStatus();
        this.transactionId = payment.getTransactionId();
        this.paymentTime = payment.getPaymentTime();
    }
}
