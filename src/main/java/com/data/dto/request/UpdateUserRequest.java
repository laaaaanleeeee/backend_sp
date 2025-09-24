package com.data.dto.request;

import com.data.enums.UserGender;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Date;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateUserRequest {
     String fullName;
     String email;
     String phone;
     LocalDate dob;
     UserGender userGender;
}
