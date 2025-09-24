package com.data.service;

import com.data.dto.request.VehicleRequestDTO;
import com.data.dto.response.VehicleResponseDTO;
import com.data.entity.User;
import com.data.entity.Vehicle;
import com.data.repository.UserRepository;
import com.data.repository.VehicleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VehicleService {
    VehicleRepository vehicleRepository;
    UserRepository userRepository;

    public VehicleResponseDTO createVehicle(VehicleRequestDTO dto, User user) {
        Vehicle vehicle = new Vehicle();
        vehicle.setLicensePlate(dto.getLicensePlate());
        vehicle.setVehicleType(dto.getVehicleType());
        vehicle.setBrand(dto.getBrand());
        vehicle.setModel(dto.getModel());
        vehicle.setColor(dto.getColor());
        vehicle.setManufactureYear(dto.getManufactureYear());

        vehicle.setUser(user);
        return new VehicleResponseDTO(vehicleRepository.save(vehicle));
    }


    public VehicleResponseDTO getVehicleById(Long id) {
        return vehicleRepository.findById(id)
                .map(VehicleResponseDTO::new)
                .orElse(null);
    }

    public List<VehicleResponseDTO> getVehiclesByUser(Long userId) {
        return vehicleRepository.findByUserId(userId)
                .stream()
                .map(VehicleResponseDTO::new)
                .collect(Collectors.toList());
    }

    public List<VehicleResponseDTO> getAllVehicles() {
        return vehicleRepository.findAll()
                .stream()
                .map(VehicleResponseDTO::new)
                .collect(Collectors.toList());
    }

    public VehicleResponseDTO updateVehicle(Long id, VehicleRequestDTO dto) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));

        vehicle.setLicensePlate(dto.getLicensePlate());
        vehicle.setVehicleType(dto.getVehicleType());
        vehicle.setBrand(dto.getBrand());
        vehicle.setModel(dto.getModel());
        vehicle.setColor(dto.getColor());
        vehicle.setManufactureYear(dto.getManufactureYear());

        return new VehicleResponseDTO(vehicleRepository.save(vehicle));
    }

    public void deleteVehicle(Long id) {
        vehicleRepository.deleteById(id);
    }
}
