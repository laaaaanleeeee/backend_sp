package com.data.dto.request;

import com.data.enums.UserGender;
import com.data.enums.UserRole;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SignUpRequestDTO {
    String username;
    String password;
    String email;
    String fullName;
    String phone;
    LocalDate dob;
    UserGender userGender;
    UserRole userRole;
}
