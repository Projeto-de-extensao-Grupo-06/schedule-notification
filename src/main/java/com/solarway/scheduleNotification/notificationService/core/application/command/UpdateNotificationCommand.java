package com.solarway.scheduleNotification.notificationService.core.application.command;

import com.solarway.scheduleNotification.notificationService.core.domain.model.ScheduleType;

import java.time.LocalDateTime;
import java.util.List;

public record UpdateNotificationCommand(
        Long scheduleId,
        String projectTitle,
        String title,
        List<RecipientCommand> recipients,
        ScheduleType type,
        LocalDateTime startDate,
        LocalDateTime endDate
) {}



