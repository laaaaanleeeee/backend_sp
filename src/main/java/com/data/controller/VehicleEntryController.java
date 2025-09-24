package com.data.controller;

import com.data.dto.request.VehicleEntryRequestDTO;
import com.data.dto.response.VehicleEntryResponseDTO;
import com.data.service.VehicleEntryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vehicle-entry")
@RequiredArgsConstructor
public class VehicleEntryController {

    private final VehicleEntryService vehicleEntryService;

    @PostMapping("/detect")
    public ResponseEntity<VehicleEntryResponseDTO> detect(@RequestBody VehicleEntryRequestDTO dto) {
        return ResponseEntity.ok(vehicleEntryService.processEntry(dto));
    }
}
