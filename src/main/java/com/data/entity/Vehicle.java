package com.data.entity;

import com.data.enums.VehicleType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "vehicles")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "license_plate", nullable = false, unique = true, length = 20)
    String licensePlate;

    @Enumerated(EnumType.STRING)
    VehicleType vehicleType;

    @Column(length = 100)
    String brand;

    @Column(length = 100)
    String model;

    @Column(length = 50)
    String color;

    @Column(name = "manufacture_year")
    Integer manufactureYear;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @OneToMany(mappedBy = "vehicle")
    List<Image> images;
}
