package com.data.service;

import com.data.dto.request.PaymentRequestDTO;
import com.data.dto.response.PaymentResponseDTO;
import com.data.entity.Booking;
import com.data.entity.ParkingLot;
import com.data.entity.Payment;
import com.data.entity.Slot;
import com.data.enums.BookingStatus;
import com.data.enums.PaymentMethod;
import com.data.enums.PaymentStatus;
import com.data.enums.SlotStatus;
import com.data.repository.BookingRepository;
import com.data.repository.PaymentRepository;
import com.data.repository.SlotRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentService {
    PaymentRepository paymentRepository;
    BookingRepository bookingRepository;
    SlotRepository slotRepository;

    @Transactional
    public PaymentResponseDTO createPayment(PaymentRequestDTO dto) {
        Booking booking = bookingRepository.findById(dto.getBookingId())
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (booking.getBookingStatus() != BookingStatus.PENDING) {
            throw new RuntimeException("Booking is not valid for payment");
        }

        Payment payment = new Payment();
        payment.setBooking(booking);
        payment.setAmount(booking.getTotalPrice());
        payment.setPaymentMethod(dto.getMethod() != null ? dto.getMethod() : PaymentMethod.MOMO);
        payment.setPaymentStatus(PaymentStatus.PENDING);
        payment.setPaymentTime(LocalDateTime.now());
        payment.setTransactionId(UUID.randomUUID().toString());

        return new PaymentResponseDTO(paymentRepository.save(payment));
    }

    @Transactional
    public PaymentResponseDTO handlePaymentCallback(String transactionId, boolean success) {
        Payment payment = paymentRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        Booking booking = payment.getBooking();

        if (success) {
            payment.setPaymentStatus(PaymentStatus.PAID);
            booking.setBookingStatus(BookingStatus.CONFIRMED);
            booking.setUpdatedAt(LocalDateTime.now());

            Slot slot = booking.getParkingSlot();
            slot.setSlotStatus(SlotStatus.OCCUPIED);
            slotRepository.save(slot);
        } else {
            payment.setPaymentStatus(PaymentStatus.FAILED);
        }

        payment.setPaymentTime(LocalDateTime.now());
        bookingRepository.save(booking);
        return new PaymentResponseDTO(paymentRepository.save(payment));
    }

    public PaymentResponseDTO getPaymentByBooking(Long bookingId) {
        return paymentRepository.findByBookingId(bookingId)
                .map(PaymentResponseDTO::new)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
    }

    public List<PaymentResponseDTO> getAllPayments() {
        return paymentRepository.findAll()
                .stream().map(PaymentResponseDTO::new).toList();
    }

    @Transactional
    public PaymentResponseDTO refundPayment(Long bookingId, String reason) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        Payment payment = paymentRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        if (payment.getPaymentStatus() != PaymentStatus.PAID) {
            throw new RuntimeException("Only paid bookings can be refunded");
        }

        payment.setPaymentStatus(PaymentStatus.REFUNDED);
        payment.setPaymentTime(LocalDateTime.now());

        booking.setBookingStatus(BookingStatus.CANCELLED);
        booking.setCancellationReason(reason);
        booking.setCancelledAt(LocalDateTime.now());
        booking.setUpdatedAt(LocalDateTime.now());

        Slot slot = booking.getParkingSlot();
        slot.setSlotStatus(SlotStatus.FREE);
        ParkingLot lot = slot.getParkingLot();
        lot.setAvailableSlots(lot.getAvailableSlots() + 1);

        slotRepository.save(slot);
        bookingRepository.save(booking);

        return new PaymentResponseDTO(paymentRepository.save(payment));
    }
}
