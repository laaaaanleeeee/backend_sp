package com.data.entity;

import com.data.enums.VehicleEntryStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Table(name = "vehicle_entry_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VehicleEntryLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "license_plate", nullable = false)
    String licensePlate;

    @Column(name = "entry_time")
    LocalDateTime entryTime;

    @Column(name = "exit_time")
    LocalDateTime exitTime;

    @Enumerated(EnumType.STRING)
    VehicleEntryStatus status;

    @Column(name = "image_url")
    String imageUrl;

    @ManyToOne
    @JoinColumn(name = "booking_id")
    Booking booking;

    @ManyToOne
    @JoinColumn(name = "parking_lot_id", nullable = false)
    ParkingLot parkingLot;

    @Column(name = "device_ip")
    String deviceIp;

    @Column(name = "note")
    String note;
}
