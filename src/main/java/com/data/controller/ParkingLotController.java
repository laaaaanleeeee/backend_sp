package com.data.controller;

import com.data.dto.request.ParkingLotRequestDTO;
import com.data.dto.response.PageDTO;
import com.data.dto.response.ParkingLotResponseDTO;
import com.data.entity.User;
import com.data.service.ParkingLotService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/parking-lots")
@CrossOrigin(origins = "http://localhost:5173")
public class ParkingLotController {
    ParkingLotService parkingLotService;

    @GetMapping
    public ResponseEntity<PageDTO<ParkingLotResponseDTO>> getAllParkingLots(@RequestParam(required = false) String name,
                                                                            @RequestParam(required = false) String city,
                                                                            @RequestParam(required = false) String ward,
                                                                            @RequestParam(required = false) Double minPrice,
                                                                            @RequestParam(required = false) Double maxPrice,
                                                                            @RequestParam(required = false) Double minRating,
                                                                            @RequestParam(required = false) Integer minSlots,
                                                                            @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        PageDTO<ParkingLotResponseDTO> parkingLots = parkingLotService.getAllParkingLots(
                name, city, ward, minPrice, maxPrice, minRating, minSlots, pageable);
        return new ResponseEntity<>(parkingLots, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ParkingLotResponseDTO> getParkingLotById(@PathVariable Long id) {
        ParkingLotResponseDTO parkingLot = parkingLotService.getParkingLotById(id);
        if (parkingLot != null) {
            return new ResponseEntity<>(parkingLot, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<ParkingLotResponseDTO> create(@RequestBody @Valid ParkingLotRequestDTO dto) {
        return new ResponseEntity<>(parkingLotService.createParkingLot(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ParkingLotResponseDTO> update(@PathVariable Long id, @RequestBody @Valid ParkingLotRequestDTO dto) {
        return ResponseEntity.ok(parkingLotService.updateParkingLot(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        parkingLotService.deleteParkingLot(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public ResponseEntity<PageDTO<ParkingLotResponseDTO>> getMyParkingLots(
            Authentication authentication,
            @PageableDefault(size = 10, sort = "id") Pageable pageable) {

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }

        User user = (User) authentication.getPrincipal();
        PageDTO<ParkingLotResponseDTO> myLots = parkingLotService.getMyParkingLots(user.getId(), pageable);
        return ResponseEntity.ok(myLots);
    }

    @PostMapping("/me")
    public ResponseEntity<ParkingLotResponseDTO> createMyParkingLot(
            @RequestBody @Valid ParkingLotRequestDTO dto,
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return new ResponseEntity<>(parkingLotService.createMyParkingLot(user.getId(), dto), HttpStatus.CREATED);
    }

    @PutMapping("/me/{id}")
    public ResponseEntity<ParkingLotResponseDTO> updateMyParkingLot(
            @PathVariable Long id,
            @RequestBody @Valid ParkingLotRequestDTO dto,
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(parkingLotService.updateMyParkingLot(id, user.getId(), dto));
    }

    @DeleteMapping("/me/{id}")
    public ResponseEntity<Void> deleteMyParkingLot(
            @PathVariable Long id,
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        parkingLotService.deleteMyParkingLot(id, user.getId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/admin/getAll")
    public ResponseEntity<PageDTO<ParkingLotResponseDTO>> adminGetAllParkingLots(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String ward,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Double minRating,
            @RequestParam(required = false) Integer minSlots,
            @PageableDefault(size = 10, sort = "id") Pageable pageable
    ) {
        return ResponseEntity.ok(
                parkingLotService.getAllParkingLots(name, city, ward, minPrice, maxPrice, minRating, minSlots, pageable)
        );
    }

    @GetMapping("admin/getById/{id}")
    public ResponseEntity<ParkingLotResponseDTO> adminGetParkingLotById(@PathVariable Long id) {
        ParkingLotResponseDTO parkingLot = parkingLotService.getParkingLotById(id);
        return parkingLot != null ? ResponseEntity.ok(parkingLot) : ResponseEntity.notFound().build();
    }

    @PostMapping("/admin/create")
    public ResponseEntity<ParkingLotResponseDTO> adminCreateParkingLot(@RequestBody @Valid ParkingLotRequestDTO dto) {
        return new ResponseEntity<>(parkingLotService.createParkingLot(dto), HttpStatus.CREATED);
    }

    @PutMapping("/admin/update/{id}")
    public ResponseEntity<ParkingLotResponseDTO> adminUpdateParkingLot(
            @PathVariable Long id,
            @RequestBody @Valid ParkingLotRequestDTO dto
    ) {
        return ResponseEntity.ok(parkingLotService.updateParkingLot(id, dto));
    }

    @DeleteMapping("/admin/delete/{id}")
    public ResponseEntity<Void> adminDeleteParkingLot(@PathVariable Long id) {
        parkingLotService.deleteParkingLot(id);
        return ResponseEntity.noContent().build();
    }

}