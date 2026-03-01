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
    public ScheduleNotification execute(UpdateNotificationCommand command){
        query.findByScheduleId(command.scheduleId())
                .orElseThrow(() -> new NotificationNotFoundException(command.scheduleId()));

        ScheduleNotification updated = ScheduleNotification.newSchedule(
                command.scheduleId(),
                command.projectTitle(),
                command.email(),
                command.phone(),
                command.type(),
                command.startDate(),
                command.endDate(),
                daysBefore
        );
        return mutation.save(updated);

    }
}
