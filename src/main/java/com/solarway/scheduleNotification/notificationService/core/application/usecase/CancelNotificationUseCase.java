package com.solarway.scheduleNotification.notificationService.core.application.usecase;

import com.solarway.scheduleNotification.notificationService.core.adapters.NotificationMutation;
import com.solarway.scheduleNotification.notificationService.core.adapters.NotificationQuery;
import com.solarway.scheduleNotification.notificationService.core.application.command.CancelNotificationCommand;
import com.solarway.scheduleNotification.notificationService.core.domain.model.ScheduleNotification;
import com.solarway.scheduleNotification.notificationService.core.domain.shared.exception.NotificationNotFoundException;

public class CancelNotificationUseCase {
    private final NotificationQuery query;
    private final NotificationMutation mutation;

    public CancelNotificationUseCase(NotificationQuery query, NotificationMutation mutation) {
        this.query = query;
        this.mutation = mutation;
    }

    public void execute(CancelNotificationCommand command) {
        query.findByScheduleId(command.scheduleId())
                .orElseThrow(() -> new NotificationNotFoundException(command.scheduleId()));

        mutation.deleteByScheduleId(command.scheduleId());
    }
}
