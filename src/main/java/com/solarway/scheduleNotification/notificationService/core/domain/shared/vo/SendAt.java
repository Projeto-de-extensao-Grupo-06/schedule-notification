package com.solarway.scheduleNotification.notificationService.core.domain.shared.vo;

import com.solarway.scheduleNotification.notificationService.core.domain.shared.exception.InvalidArgumentException;

import java.time.LocalDateTime;

public class SendAt {
    private final LocalDateTime value;

    private SendAt(LocalDateTime value) {
        this.value = value;
    }

    public static SendAt of(LocalDateTime startDate, int daysBefore) {
        if (startDate == null) {
            throw new InvalidArgumentException("startDate não pode ser nulo.");
        }
        if (daysBefore < 0) {
            throw new InvalidArgumentException("daysBefore não pode ser negativo.");
        }
        return new SendAt(startDate.minusDays(daysBefore));
    }

    public LocalDateTime getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
