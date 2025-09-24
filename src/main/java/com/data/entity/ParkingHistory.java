package com.data.entity;

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
@Table(name = "parking_history")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ParkingHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "vehicle_id", nullable = false)
    Vehicle vehicle;

    @ManyToOne
    @JoinColumn(name = "parking_slot_id", nullable = false)
    Slot parkingSlot;

    @ManyToOne
    @JoinColumn(name = "parking_lot_id", nullable = false)
    ParkingLot parkingLot;

    @Column(name = "entry_time", nullable = false)
    LocalDateTime entryTime;

    @Column(name = "exit_time")
    LocalDateTime exitTime;

    @Column(name = "total_cost")
    Double totalCost;

    @ManyToOne
    @JoinColumn(name = "booking_id")
    Booking booking;
}
