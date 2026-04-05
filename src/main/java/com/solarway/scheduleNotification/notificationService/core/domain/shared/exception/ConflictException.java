package com.solarway.scheduleNotification.notificationService.core.domain.shared.exception;

public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }
}
