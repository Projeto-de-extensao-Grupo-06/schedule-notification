package com.solarway.scheduleNotification.notificationService.infraestructure.persistence.jpa;

import com.solarway.scheduleNotification.notificationService.core.domain.model.NotificationStatus;
import com.solarway.scheduleNotification.notificationService.core.domain.model.ScheduleType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class ScheduleNotificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private Long scheduleId;
    private String projectTitle;
    private String email;
    private String phone;

    @Enumerated(EnumType.STRING)
    private ScheduleType type;


    @Enumerated(EnumType.STRING)
    private NotificationStatus status;

    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime sendAt;
    private LocalDateTime createdAt;

    protected ScheduleNotificationEntity () {}

}
