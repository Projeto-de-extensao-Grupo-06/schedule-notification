package com.solarway.scheduleNotification.notificationService.core.adapters;

import com.solarway.scheduleNotification.notificationService.core.domain.model.ScheduleNotification;

public interface NotificationSender {
    void send(ScheduleNotification notification);
}
