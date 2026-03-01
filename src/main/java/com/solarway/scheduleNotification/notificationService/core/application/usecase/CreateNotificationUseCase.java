package com.solarway.scheduleNotification.notificationService.core.application.usecase;

import com.solarway.scheduleNotification.notificationService.core.adapters.NotificationMutation;
import com.solarway.scheduleNotification.notificationService.core.application.command.CreateNotificationCommand;
import com.solarway.scheduleNotification.notificationService.core.domain.model.ScheduleNotification;

public class CreateNotificationUseCase {
    private final NotificationMutation mutation;
    private final int daysBefore;

    public CreateNotificationUseCase(NotificationMutation mutation, int daysBefore) {
        this.mutation = mutation;
        this.daysBefore = daysBefore;
    }

    public ScheduleNotification execute(CreateNotificationCommand command) {
        ScheduleNotification notification = ScheduleNotification.newSchedule(
                command.scheduleId(),
                command.projectTitle(),
                command.email(),
                command.phone(),
                command.type(),
                command.startDate(),
                command.endDate(),
                daysBefore
        );
        return mutation.save(notification);
    }
}
