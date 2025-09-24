package com.data.service;

import com.data.dto.request.BookingRequestDTO;
import com.data.dto.response.BookingResponseDTO;
import com.data.dto.response.PageDTO;
import com.data.entity.*;
import com.data.enums.BookingStatus;
import com.data.enums.SlotStatus;
import com.data.exception.BadRequestException;
import com.data.exception.NotFoundException;
import com.data.repository.*;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookingService {
    BookingRepository bookingRepository;
    UserRepository userRepository;
    ParkingLotRepository parkingLotRepository;
    SlotRepository slotRepository;
    VehicleRepository vehicleRepository;
    PricingRepository pricingRepository;
    VoucherRepository voucherRepository;

    @Transactional
    public BookingResponseDTO createBooking(BookingRequestDTO dto, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found"));

        ParkingLot lot = parkingLotRepository.findById(dto.getParkingLotId())
                .orElseThrow(() -> new NotFoundException("Parking lot not found"));

        if (lot.getAvailableSlots() <= 0) {
            throw new BadRequestException("No available slots in this parking lot");
        }

        Slot slot = slotRepository.findById(dto.getParkingSlotId())
                .orElseThrow(() -> new NotFoundException("Slot not found"));

        if (slot.getSlotStatus() != SlotStatus.FREE) {
            throw new BadRequestException("Slot is not available");
        }

        Vehicle vehicle = vehicleRepository.findById(dto.getVehicleId())
                .orElseThrow(() -> new NotFoundException("Vehicle not found"));

        List<Pricing> pricings = pricingRepository.findByParkingLot_IdAndVehicleType(
                lot.getId(),
                vehicle.getVehicleType()
        );
        if (pricings.isEmpty()) {
            throw new BadRequestException("No pricing found for this vehicle type");
        }

        Pricing pricing = pricings.stream()
                .filter(p -> (p.getStartTime() == null || !dto.getStartTime().isBefore(p.getStartTime())))
                .filter(p -> (p.getEndTime() == null || !dto.getEndTime().isAfter(p.getEndTime())))
                .findFirst()
                .orElseThrow(() -> new BadRequestException("No valid pricing for this time range"));

        long minutes = ChronoUnit.MINUTES.between(dto.getStartTime(), dto.getEndTime());
        if (minutes <= 0) throw new BadRequestException("End time must be after start time");

        double totalPrice = (minutes / 60.0) * pricing.getPricePerHour();
        if (dto.getVoucherId() != null) {
            Voucher voucher = voucherRepository.findById(dto.getVoucherId())
                    .orElseThrow(() -> new NotFoundException("Voucher not found"));
            totalPrice -= totalPrice * voucher.getDiscount() / 100.0;
        }

        slot.setSlotStatus(SlotStatus.RESERVED);
        slotRepository.save(slot);

        lot.setAvailableSlots(lot.getAvailableSlots() - 1);
        parkingLotRepository.save(lot);

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setParkingLot(lot);
        booking.setParkingSlot(slot);
        booking.setVehicle(vehicle);
        booking.setPricing(pricing);
        booking.setStartTime(dto.getStartTime());
        booking.setEndTime(dto.getEndTime());
        booking.setTotalPrice(totalPrice);
        booking.setBookingStatus(BookingStatus.PENDING);
        booking.setCreatedAt(LocalDateTime.now());
        booking.setUpdatedAt(LocalDateTime.now());
        booking.setExpireAt(LocalDateTime.now().plusMinutes(15));

        return new BookingResponseDTO(bookingRepository.save(booking));
    }

    @Transactional
    public BookingResponseDTO cancelBooking(Long id, String reason, String username) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Booking not found"));

        if (!booking.getUser().getUsername().equals(username)) {
            throw new BadRequestException("You cannot cancel others' bookings");
        }

        if (booking.getBookingStatus() == BookingStatus.CANCELLED) {
            throw new BadRequestException("Booking already cancelled");
        }

        booking.setBookingStatus(BookingStatus.CANCELLED);
        booking.setCancelledAt(LocalDateTime.now());
        booking.setCancellationReason(reason);
        booking.setUpdatedAt(LocalDateTime.now());

        Slot slot = booking.getParkingSlot();
        slot.setSlotStatus(SlotStatus.FREE);
        slotRepository.save(slot);

        ParkingLot lot = booking.getParkingLot();
        lot.setAvailableSlots(lot.getAvailableSlots() + 1);
        parkingLotRepository.save(lot);

        return new BookingResponseDTO(bookingRepository.save(booking));
    }

    @Transactional
    public BookingResponseDTO confirmBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Booking not found"));

        if (booking.getBookingStatus() != BookingStatus.PENDING) {
            throw new BadRequestException("Booking is not pending");
        }

        booking.setBookingStatus(BookingStatus.CONFIRMED);
        booking.setUpdatedAt(LocalDateTime.now());

        Slot slot = booking.getParkingSlot();
        slot.setSlotStatus(SlotStatus.OCCUPIED);
        slotRepository.save(slot);

        return new BookingResponseDTO(bookingRepository.save(booking));
    }

    @Transactional
    public BookingResponseDTO completeBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Booking not found"));

        if (booking.getBookingStatus() != BookingStatus.CONFIRMED) {
            throw new BadRequestException("Booking is not confirmed");
        }

        booking.setBookingStatus(BookingStatus.COMPLETED);
        booking.setUpdatedAt(LocalDateTime.now());

        Slot slot = booking.getParkingSlot();
        slot.setSlotStatus(SlotStatus.FREE);
        slotRepository.save(slot);

        ParkingLot lot = booking.getParkingLot();
        lot.setAvailableSlots(lot.getAvailableSlots() + 1);
        parkingLotRepository.save(lot);

        return new BookingResponseDTO(bookingRepository.save(booking));
    }

    @Transactional
    public void expirePendingBookings() {
        List<Booking> expired = bookingRepository.findByBookingStatusAndExpireAtBefore(
                BookingStatus.PENDING, LocalDateTime.now());

        for (Booking booking : expired) {
            booking.setBookingStatus(BookingStatus.CANCELLED);
            booking.setCancelledAt(LocalDateTime.now());
            booking.setCancellationReason("Expired pending booking");

            Slot slot = booking.getParkingSlot();
            slot.setSlotStatus(SlotStatus.FREE);
            slotRepository.save(slot);

            ParkingLot lot = booking.getParkingLot();
            lot.setAvailableSlots(lot.getAvailableSlots() + 1);
            parkingLotRepository.save(lot);

            bookingRepository.save(booking);
        }
    }

    public BookingResponseDTO getBookingById(Long id) {
        return new BookingResponseDTO(
                bookingRepository.findById(id)
                        .orElseThrow(() -> new NotFoundException("Booking not found"))
        );
    }

    public PageDTO<BookingResponseDTO> getBookingsByUser(String username, Pageable pageable) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found"));

        return PageDTO.of(
                bookingRepository.findByUser_Id(user.getId(), pageable)
                        .map(BookingResponseDTO::new)
        );
    }

    public PageDTO<BookingResponseDTO> searchBookings(
            Pageable pageable,
            String username,
            BookingStatus status,
            Long lotId
    ) {
        Page<Booking> page = bookingRepository.searchBookings(username, status, lotId, pageable);
        return PageDTO.of(page.map(BookingResponseDTO::new));
    }

    @Transactional
    public BookingResponseDTO updateBookingStatus(Long id, BookingStatus status, String reason) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Booking not found"));

        booking.setBookingStatus(status);
        booking.setUpdatedAt(LocalDateTime.now());

        if (status == BookingStatus.CANCELLED) {
            booking.setCancelledAt(LocalDateTime.now());
            booking.setCancellationReason(reason != null ? reason : "Cancelled by admin");

            Slot slot = booking.getParkingSlot();
            if (slot != null) {
                slot.setSlotStatus(SlotStatus.FREE);
                slotRepository.save(slot);
            }

            ParkingLot lot = booking.getParkingLot();
            if (lot != null) {
                lot.setAvailableSlots(lot.getAvailableSlots() + 1);
                parkingLotRepository.save(lot);
            }
        }

        return new BookingResponseDTO(bookingRepository.save(booking));
    }

    @Transactional
    public void deleteBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Booking not found"));

        if (booking.getBookingStatus() != BookingStatus.CANCELLED) {
            Slot slot = booking.getParkingSlot();
            if (slot != null) {
                slot.setSlotStatus(SlotStatus.FREE);
                slotRepository.save(slot);
            }

            ParkingLot lot = booking.getParkingLot();
            if (lot != null) {
                lot.setAvailableSlots(lot.getAvailableSlots() + 1);
                parkingLotRepository.save(lot);
            }
        }

        bookingRepository.delete(booking);
    }

    public PageDTO<BookingResponseDTO> getBookingsByOwner(String username, Pageable pageable, BookingStatus status) {
        User owner = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Owner not found"));

        Page<Booking> page = bookingRepository.findByParkingLot_Owner_IdAndStatus(
                owner.getId(), status, pageable
        );

        return PageDTO.of(page.map(BookingResponseDTO::new));
    }

    @Transactional
    public BookingResponseDTO confirmBookingAsOwner(Long id, String username) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Booking not found"));

        if (!booking.getParkingLot().getOwner().getUsername().equals(username)) {
            throw new BadRequestException("You cannot confirm others' bookings");
        }

        if (booking.getBookingStatus() != BookingStatus.PENDING) {
            throw new BadRequestException("Booking không ở trạng thái pending");
        }

        booking.setBookingStatus(BookingStatus.CONFIRMED);
        booking.setUpdatedAt(LocalDateTime.now());

        Slot slot = booking.getParkingSlot();
        slot.setSlotStatus(SlotStatus.OCCUPIED);
        slotRepository.save(slot);

        return new BookingResponseDTO(bookingRepository.save(booking));
    }

    @Transactional
    public BookingResponseDTO completeBookingAsOwner(Long id, String username) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Booking not found"));

        if (!booking.getParkingLot().getOwner().getUsername().equals(username)) {
            throw new BadRequestException("You cannot complete others' bookings");
        }

        if (booking.getBookingStatus() != BookingStatus.CONFIRMED) {
            throw new BadRequestException("Booking is not confirmed");
        }

        booking.setBookingStatus(BookingStatus.COMPLETED);
        booking.setUpdatedAt(LocalDateTime.now());

        Slot slot = booking.getParkingSlot();
        slot.setSlotStatus(SlotStatus.FREE);
        slotRepository.save(slot);

        ParkingLot lot = booking.getParkingLot();
        lot.setAvailableSlots(lot.getAvailableSlots() + 1);
        parkingLotRepository.save(lot);

        return new BookingResponseDTO(bookingRepository.save(booking));
    }

    @Transactional
    public BookingResponseDTO cancelBookingAsOwner(Long id, String reason, String username) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Booking not found"));

        if (!booking.getParkingLot().getOwner().getUsername().equals(username)) {
            throw new BadRequestException("You cannot cancel bookings for lots you don't own");
        }

        if (booking.getBookingStatus() == BookingStatus.CANCELLED) {
            throw new BadRequestException("Booking already cancelled");
        }

        booking.setBookingStatus(BookingStatus.CANCELLED);
        booking.setCancelledAt(LocalDateTime.now());
        booking.setCancellationReason(reason);
        booking.setUpdatedAt(LocalDateTime.now());

        Slot slot = booking.getParkingSlot();
        slot.setSlotStatus(SlotStatus.FREE);
        slotRepository.save(slot);

        ParkingLot lot = booking.getParkingLot();
        lot.setAvailableSlots(lot.getAvailableSlots() + 1);
        parkingLotRepository.save(lot);

        return new BookingResponseDTO(bookingRepository.save(booking));
    }
}
