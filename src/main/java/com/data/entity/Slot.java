package com.data.entity;

import com.data.enums.SlotStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "parking_slots",
        uniqueConstraints = @UniqueConstraint(columnNames = {"slot_number", "parking_lot_id"}))
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Slot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false, name = "slot_number", length = 50)
    String slotNumber;

    @Enumerated(EnumType.STRING)
    SlotStatus slotStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parking_lot_id", nullable = false)
    ParkingLot parkingLot;

    @JsonIgnore
    @OneToMany(mappedBy = "parkingSlot")
    List<Booking> bookings = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "parkingSlot")
    List<ParkingHistory> parkingHistories = new ArrayList<>();
}
