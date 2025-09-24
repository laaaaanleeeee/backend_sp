package com.data.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class VehicleEntryResponseDTO {
    String licensePlate;
    boolean allowed;
    String message;
    LocalDateTime timestamp;
}
