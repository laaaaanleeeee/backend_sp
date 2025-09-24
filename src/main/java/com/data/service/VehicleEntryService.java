package com.data.service;

import com.data.dto.request.VehicleEntryRequestDTO;
import com.data.dto.response.VehicleEntryResponseDTO;
import com.data.entity.Booking;
import com.data.entity.Vehicle;
import com.data.entity.VehicleEntryLog;
import com.data.enums.VehicleEntryStatus;
import com.data.repository.BookingRepository;
import com.data.repository.VehicleEntryLogRepository;
import com.data.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class VehicleEntryService {

    VehicleRepository vehicleRepository;
    BookingRepository bookingRepository;
    VehicleEntryLogRepository vehicleEntryLogRepository;

    public VehicleEntryResponseDTO processEntry(VehicleEntryRequestDTO dto) {
        LocalDateTime now = LocalDateTime.now();

        Optional<Vehicle> vehicleOpt = vehicleRepository.findByLicensePlate(dto.getLicensePlate());
        if (vehicleOpt.isEmpty()) {
            saveLog(dto, VehicleEntryStatus.DENIED, "Không tìm thấy xe trong hệ thống", null);
            return VehicleEntryResponseDTO.builder()
                    .licensePlate(dto.getLicensePlate())
                    .allowed(false)
                    .message("Xe chưa đăng ký")
                    .timestamp(now)
                    .build();
        }

        Vehicle vehicle = vehicleOpt.get();

        Optional<Booking> bookingOpt = bookingRepository.findActiveBookingForVehicle(
                vehicle.getId(), now);
        if (bookingOpt.isEmpty()) {
            saveLog(dto, VehicleEntryStatus.DENIED, "Không có booking hợp lệ", null);
            return VehicleEntryResponseDTO.builder()
                    .licensePlate(dto.getLicensePlate())
                    .allowed(false)
                    .message("Không có booking hợp lệ")
                    .timestamp(now)
                    .build();
        }

        Booking booking = bookingOpt.get();

        saveLog(dto, VehicleEntryStatus.ALLOWED, "Xe có booking hợp lệ", booking);

        return VehicleEntryResponseDTO.builder()
                .licensePlate(dto.getLicensePlate())
                .allowed(true)
                .message("Cho phép vào bãi")
                .timestamp(now)
                .build();
    }

    private void saveLog(VehicleEntryRequestDTO dto, VehicleEntryStatus status, String note, Booking booking) {
        VehicleEntryLog log = new VehicleEntryLog();
        log.setLicensePlate(dto.getLicensePlate());
        log.setImageUrl(dto.getImageUrl());
        log.setDeviceIp(dto.getDeviceIp());
        log.setStatus(status);
        log.setNote(note);
        log.setEntryTime(LocalDateTime.now());
        if (booking != null) {
            log.setBooking(booking);
            log.setParkingLot(booking.getParkingLot());
        }
        vehicleEntryLogRepository.save(log);
    }
}
