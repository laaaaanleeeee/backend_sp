package com.data.entity;

import com.data.enums.BarrierCommandType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Table(name = "barrier_command_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BarrierCommandLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Enumerated(EnumType.STRING)
    BarrierCommandType commandType;

    @Column(name = "timestamp", nullable = false)
    LocalDateTime timestamp;

    @Column(name = "success")
    Boolean success;

    @Column(name = "device_ip")
    String deviceIp;

    @Column(name = "reason")
    String reason;

    @ManyToOne
    @JoinColumn(name = "triggered_by")
    User triggeredBy;
}
