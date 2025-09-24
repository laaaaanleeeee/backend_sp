package com.data.dto.request;

import com.data.enums.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentRequestDTO {

    @NotNull(message = "BookingId is required")
    Long bookingId;

    PaymentMethod method;
}