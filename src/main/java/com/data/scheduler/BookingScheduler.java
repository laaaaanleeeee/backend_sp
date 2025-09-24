package com.data.scheduler;

import com.data.service.BookingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class BookingScheduler {

    private final BookingService bookingService;

    @Scheduled(fixedRate = 60000)
    public void expirePendingBookings() {
        log.info("Running expirePendingBookings scheduler...");
        bookingService.expirePendingBookings();
    }
}
