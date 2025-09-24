package com.data.dto.request;

import com.data.enums.UserGender;
import com.data.enums.UserRole;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateUserAdminRequest {
    String fullName;
    String email;
    String phone;
    LocalDate dob;
    UserGender userGender;
    UserRole userRole;
}
