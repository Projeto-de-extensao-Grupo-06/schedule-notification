package com.solarway.scheduleNotification.notificationService.core.domain.shared.exception;

public class NotificationNotFoundException extends RuntimeException {
    public NotificationNotFoundException(Long scheduleId) {

      super("Notification not found for this schedule: " + scheduleId);
    }
}
