package com.solarway.scheduleNotification.notificationService.core.application.command;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RecipientCommand(
        @JsonProperty("email") String email,
        @JsonProperty("phone") String phone
) {}
