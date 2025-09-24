package com.data.service;

import com.data.dto.response.SlotResponseDTO;
import com.data.entity.Slot;
import com.data.repository.SlotRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SlotService {
    SlotRepository slotRepository;

    public List<SlotResponseDTO> getSlotsByParkingLot(Long parkingLotId) {
        List<Slot> slots = slotRepository.findByParkingLotId(parkingLotId);
        return slots.stream()
                .map(slot -> new SlotResponseDTO(slot.getId(), slot.getSlotNumber(), slot.getSlotStatus()))
                .collect(Collectors.toList());
    }
}
