package com.data.dto.response;

import com.data.entity.User;
import com.data.enums.UserGender;
import com.data.enums.UserRole;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Date;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponseDTO {
     Long id;
     String username;
     String email;
     String fullName;
     String phone;
     String createdAt;
     UserRole userRole;
     LocalDate dob;
     UserGender userGender;

    public UserResponseDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.fullName = user.getFullName();
        this.phone = user.getPhone();
        this.createdAt = user.getCreatedAt().toString();
        this.userRole = user.getUserRole();
        this.dob = user.getDob();
        this.userGender = user.getUserGender();
    }
}