package com.data.entity;

import com.data.enums.ParkingLotStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;
import java.util.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "parking_lots")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ParkingLot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false, length = 100)
    String name;

    @Column(nullable = false)
    String address;

    @Column(length = 100)
    String city;

    @Column(length = 100)
    String ward;

    Double latitude;
    Double longitude;

    @Column(name = "total_slots", nullable = false)
    Integer totalSlots;

    @Column(name = "available_slots", nullable = false)
    Integer availableSlots;

    @Column(length = 255)
    String description;

    @Enumerated(EnumType.STRING)
    ParkingLotStatus parkingLotStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    User owner;

    @Column(name = "created_at", nullable = false)
    LocalDateTime createdAt;

    @Column(name = "updated_at")
    LocalDateTime updatedAt;

    @JsonIgnore
    @OneToMany(mappedBy = "parkingLot", fetch = FetchType.LAZY)
    List<Pricing> pricings = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "parkingLot", fetch = FetchType.LAZY)
    List<Image> images = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "parkingLot", fetch = FetchType.LAZY)
    List<Review> reviews = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "parkingLot", fetch = FetchType.LAZY)
    List<Slot> slots = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "parkingLot", fetch = FetchType.LAZY)
    List<Sensor> sensors = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "parkingLot", fetch = FetchType.LAZY)
    List<Booking> bookings = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "parkingLot", fetch = FetchType.LAZY)
    List<Voucher> vouchers = new ArrayList<>();
}
