package com.solarway.scheduleNotification.notificationService.core.application.command;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.solarway.scheduleNotification.notificationService.core.domain.model.ScheduleType;

import java.time.LocalDateTime;
import java.util.List;

public record CreateNotificationCommand(
        @JsonProperty("scheduleId") Long scheduleId,
        @JsonProperty("projectTitle") String projectTitle,
        @JsonProperty("title") String title,
        @JsonProperty("recipients") List<RecipientCommand> recipients,
        @JsonProperty("type") ScheduleType type,
        @JsonProperty("startDate") LocalDateTime startDate,
        @JsonProperty("endDate") LocalDateTime endDate
) {
}

