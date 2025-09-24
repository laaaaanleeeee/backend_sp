package com.data.dto.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VehicleEntryRequestDTO {
    String licensePlate;
    String imageUrl;
    String deviceIp;
}
