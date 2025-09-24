package com.data.dto.response;

import com.data.enums.SlotStatus;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SlotResponseDTO {
    Long id;
    String slotNumber;
    SlotStatus slotStatus;

    public SlotResponseDTO(Long id, String slotNumber, SlotStatus slotStatus) {
        this.id = id;
        this.slotNumber = slotNumber;
        this.slotStatus = slotStatus;
    }
}
