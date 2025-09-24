package com.data.dto.response;

import com.data.entity.Booking;
import com.data.enums.BookingStatus;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingResponseDTO {
    Long id;
    String startTime;
    String endTime;
    Double totalPrice;
    BookingStatus bookingStatus;
    Long userId;
    Long parkingLotId;
    Long parkingSlotId;
    String userName;
    String parkingLotName;
    String slotName;
    Long vehicleId;
    Long voucherId;
    String createdAt;
    String updatedAt;
    String cancelledAt;
    String expireAt;
    String cancellationReason;
    String address;

    String licensePlate;

    public BookingResponseDTO(Booking booking) {
        this.id = booking.getId();
        this.startTime = booking.getStartTime() != null ? booking.getStartTime().toString() : null;
        this.endTime = booking.getEndTime() != null ? booking.getEndTime().toString() : null;
        this.totalPrice = booking.getTotalPrice();
        this.bookingStatus = booking.getBookingStatus();
        this.userId = booking.getUser() != null ? booking.getUser().getId() : null;
        this.parkingLotId = booking.getParkingLot() != null ? booking.getParkingLot().getId() : null;
        this.parkingSlotId = booking.getParkingSlot() != null ? booking.getParkingSlot().getId() : null;
        this.vehicleId = booking.getVehicle() != null ? booking.getVehicle().getId() : null;
        this.voucherId = booking.getVoucher() != null ? booking.getVoucher().getId() : null;
        this.createdAt = booking.getCreatedAt() != null ? booking.getCreatedAt().toString() : null;
        this.updatedAt = booking.getUpdatedAt() != null ? booking.getUpdatedAt().toString() : null;
        this.cancelledAt = booking.getCancelledAt() != null ? booking.getCancelledAt().toString() : null;
        this.expireAt = booking.getExpireAt() != null ? booking.getExpireAt().toString() : null;
        this.cancellationReason = booking.getCancellationReason();
        this.parkingLotName = booking.getParkingLot() != null? booking.getParkingLot().getName() : null;
        this.slotName = booking.getParkingSlot() != null? booking.getParkingSlot().getSlotNumber() : null;
        this.userName = booking.getUser() != null? booking.getUser().getFullName() : null;
        this.address = booking.getParkingLot() != null? booking.getParkingLot().getAddress() : null;
        this.licensePlate = booking.getVehicle() != null ? booking.getVehicle().getLicensePlate() : null;
    }
}
