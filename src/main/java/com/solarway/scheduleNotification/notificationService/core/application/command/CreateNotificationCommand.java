package com.solarway.scheduleNotification.notificationService.core.application.command;

import com.solarway.scheduleNotification.notificationService.core.domain.model.ScheduleType;

import java.time.LocalDateTime;

public record CreateNotificationCommand(
        Long scheduleId,
        String projectTitle,
        String email,
        String phone,
        ScheduleType type,
        LocalDateTime startDate,
        LocalDateTime endDate
) {
}

