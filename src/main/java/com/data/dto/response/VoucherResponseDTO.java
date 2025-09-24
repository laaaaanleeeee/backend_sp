package com.data.dto.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VoucherResponseDTO {
    Long id;
    String code;
    Double discount;
    String expiryDate;
}