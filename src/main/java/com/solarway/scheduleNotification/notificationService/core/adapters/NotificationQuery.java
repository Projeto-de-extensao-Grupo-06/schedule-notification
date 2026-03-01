package com.solarway.scheduleNotification.notificationService.core.adapters;

import com.solarway.scheduleNotification.notificationService.core.domain.model.ScheduleNotification;

import java.util.List;
import java.util.Optional;

public interface NotificationQuery {
    Optional<ScheduleNotification> findByScheduleId(Long scheduleId);
    List<ScheduleNotification> findAllPendingDue();
}
