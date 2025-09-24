package com.data.controller;

import com.data.dto.response.SlotResponseDTO;
import com.data.service.SlotService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/slots")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@CrossOrigin(origins = "http://localhost:5173")
public class SlotController {

    SlotService slotService;

    @GetMapping("/parking-lot/{parkingLotId}")
    public ResponseEntity<List<SlotResponseDTO>> getSlotsByParkingLot(@PathVariable Long parkingLotId) {
        List<SlotResponseDTO> slots = slotService.getSlotsByParkingLot(parkingLotId);
        return ResponseEntity.ok(slots);
    }
}
