package com.data.dto.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingRequestDTO {
    LocalDateTime startTime;
    LocalDateTime endTime;
    Long parkingLotId;
    Long parkingSlotId;
    Long vehicleId;
    Long voucherId;
}
