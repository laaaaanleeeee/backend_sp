package com.data.controller;

import com.data.dto.request.BookingRequestDTO;
import com.data.dto.response.BookingResponseDTO;
import com.data.dto.response.PageDTO;
import com.data.enums.BookingStatus;
import com.data.service.BookingService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookingController {
    BookingService bookingService;

    @PostMapping
    public ResponseEntity<BookingResponseDTO> createBooking(
            @RequestBody BookingRequestDTO dto,
            Authentication auth) {
        return ResponseEntity.ok(
                bookingService.createBooking(dto, auth.getName())
        );
    }

    @GetMapping("/me")
    public ResponseEntity<PageDTO<BookingResponseDTO>> getMyBookings(
            Pageable pageable,
            Authentication auth) {
        return ResponseEntity.ok(
                bookingService.getBookingsByUser(auth.getName(), pageable)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingResponseDTO> getBookingById(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.getBookingById(id));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<BookingResponseDTO> cancelBooking(
            @PathVariable Long id,
            @RequestParam String reason,
            Authentication auth) {
        return ResponseEntity.ok(
                bookingService.cancelBooking(id, reason, auth.getName())
        );
    }

    @PutMapping("/{id}/confirm")
    public ResponseEntity<BookingResponseDTO> confirmBooking(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.confirmBooking(id));
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<BookingResponseDTO> completeBooking(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.completeBooking(id));
    }

    @GetMapping("/admin/all-bookings")
    public ResponseEntity<PageDTO<BookingResponseDTO>> searchBookings(
            Pageable pageable,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) BookingStatus status,
            @RequestParam(required = false) Long lotId
    ) {
        return ResponseEntity.ok(
                bookingService.searchBookings(pageable, username, status, lotId)
        );
    }

    @PutMapping("admin/update/{id}")
    public ResponseEntity<BookingResponseDTO> updateBookingStatus(
            @PathVariable Long id,
            @RequestParam BookingStatus status,
            @RequestParam(required = false) String reason
    ) {
        return ResponseEntity.ok(
                bookingService.updateBookingStatus(id, status, reason)
        );
    }

    @DeleteMapping("/admin/delete/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
        bookingService.deleteBooking(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/owner/my-lot-bookings")
    public ResponseEntity<PageDTO<BookingResponseDTO>> getBookingsByOwner(
            Pageable pageable,
            @RequestParam(required = false) BookingStatus status,
            Authentication auth
    ) {
        return ResponseEntity.ok(
                bookingService.getBookingsByOwner(auth.getName(), pageable, status)
        );
    }

    @PutMapping("/owner/confirm/{id}")
    public ResponseEntity<BookingResponseDTO> confirmBookingAsOwner(
            @PathVariable Long id,
            Authentication auth
    ) {
        return ResponseEntity.ok(
                bookingService.confirmBookingAsOwner(id, auth.getName())
        );
    }

    @PutMapping("/owner/cancel/{id}")
    public ResponseEntity<BookingResponseDTO> cancelBookingAsOwner(
            @PathVariable Long id,
            @RequestParam String reason,
            Authentication auth
    ) {
        return ResponseEntity.ok(
                bookingService.cancelBookingAsOwner(id, reason, auth.getName())
        );
    }

    @PutMapping("/owner/complete/{id}")
    public ResponseEntity<BookingResponseDTO> completeBookingAsOwner(
            @PathVariable Long id,
            Authentication auth
    ) {
        return ResponseEntity.ok(
                bookingService.completeBookingAsOwner(id, auth.getName())
        );
    }

}