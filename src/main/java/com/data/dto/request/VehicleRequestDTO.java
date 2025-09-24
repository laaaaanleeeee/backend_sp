package com.data.dto.request;

import com.data.enums.VehicleType;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VehicleRequestDTO {
    String licensePlate;
    VehicleType vehicleType;
    String brand;
    String model;
    String color;
    Integer manufactureYear;
}
