package com.solarway.scheduleNotification.notificationService.core.adapters;

import com.solarway.scheduleNotification.notificationService.core.domain.model.ScheduleNotification;
import org.springframework.transaction.annotation.Transactional;

public interface NotificationMutation {
    ScheduleNotification save(ScheduleNotification notification);
    void deleteByScheduleId(Long scheduleId);
}
