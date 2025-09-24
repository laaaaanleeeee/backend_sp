package com.data.dto.request;

import com.data.enums.ParkingLotStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ParkingLotRequestDTO {
    @NotBlank
    String name;

    @NotBlank
    String address;

    String city;
    String ward;
    Double latitude;
    Double longitude;

    @NotNull
    Integer totalSlots;

    @NotNull
    Integer availableSlots;

    String description;

    ParkingLotStatus parkingLotStatus;

    @NotNull
    Long ownerId;
}
