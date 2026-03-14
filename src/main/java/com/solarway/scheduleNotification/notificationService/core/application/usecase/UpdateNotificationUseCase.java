package com.solarway.scheduleNotification.notificationService.core.application.usecase;

import com.solarway.scheduleNotification.notificationService.core.adapters.NotificationMutation;
import com.solarway.scheduleNotification.notificationService.core.adapters.NotificationQuery;
import com.solarway.scheduleNotification.notificationService.core.application.command.UpdateNotificationCommand;
import com.solarway.scheduleNotification.notificationService.core.domain.model.ScheduleNotification;
import com.solarway.scheduleNotification.notificationService.core.domain.shared.exception.NotificationNotFoundException;

public class UpdateNotificationUseCase {
    private final NotificationQuery query;
    private final NotificationMutation mutation;
    private final int daysBefore;

    public UpdateNotificationUseCase(NotificationQuery query, NotificationMutation mutation, int daysBefore) {
        this.query = query;
        this.mutation = mutation;
        this.daysBefore = daysBefore;
    }
    public ScheduleNotification execute(Long scheduleId, UpdateNotificationCommand command) {
        ScheduleNotification existing = query.findByScheduleId(scheduleId)
                .orElseThrow(() -> new NotificationNotFoundException(scheduleId));

        ScheduleNotification updated = ScheduleNotification.existing(
                existing.getId(),
                scheduleId,
                command.projectTitle(),
                command.email(),
                command.phone(),
                command.type(),
                existing.getStatus(),
                command.startDate(),
                command.endDate(),
                command.startDate().minusDays(daysBefore),
                existing.getCreatedAt()
        );

        return mutation.save(updated);
    }
}
