package com.data.entity;

import com.data.enums.PaymentMethod;
import com.data.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "payments")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    Double amount;

    @Enumerated(EnumType.STRING)
    PaymentMethod paymentMethod;

    @Column(name = "payment_time")
    LocalDateTime paymentTime;

    @Enumerated(EnumType.STRING)
    PaymentStatus paymentStatus;

    @Column(name = "transaction_id")
    String transactionId;

    @OneToOne
    @JoinColumn(name = "booking_id", nullable = false)
    Booking booking;
}