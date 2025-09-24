package com.data.dto.response;

import com.data.entity.Vehicle;
import com.data.enums.VehicleType;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VehicleResponseDTO {
    Long id;
    String licensePlate;
    VehicleType vehicleType;
    String brand;
    String model;
    String color;
    Integer manufactureYear;
    Long userId;
    String username;

    public VehicleResponseDTO(Vehicle vehicle) {
        this.id = vehicle.getId();
        this.licensePlate = vehicle.getLicensePlate();
        this.vehicleType = vehicle.getVehicleType();
        this.brand = vehicle.getBrand();
        this.model = vehicle.getModel();
        this.color = vehicle.getColor();
        this.manufactureYear = vehicle.getManufactureYear();
        if (vehicle.getUser() != null) {
            this.userId = vehicle.getUser().getId();
            this.username = vehicle.getUser().getUsername();
        }
    }
}
