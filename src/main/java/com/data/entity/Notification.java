package com.data.entity;

import com.data.enums.NotificationStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "notifications")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    String message;

    @Column(name = "created_at", nullable = false)
    LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    NotificationStatus notificationStatus;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    User user;
}
