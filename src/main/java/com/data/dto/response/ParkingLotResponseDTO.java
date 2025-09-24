package com.data.dto.response;

import com.data.enums.ParkingLotStatus;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ParkingLotResponseDTO {
     Long id;
     String name;
     String address;
     String city;
     String ward;
     Double latitude;
     Double longitude;
     Integer totalSlots;
     Integer availableSlots;
     String description;
     ParkingLotStatus parkingLotStatus;
     UserResponseDTO owner;
     List<PricingResponseDTO> pricings;
     List<ImageResponseDTO> images;
     List<ReviewResponseDTO> reviews;
     String createdAt;
     String updatedAt;
}