package com.solarway.scheduleNotification.notificationService.core.application.usecase;

import com.solarway.scheduleNotification.notificationService.core.adapters.NotificationMutation;
import com.solarway.scheduleNotification.notificationService.core.adapters.NotificationQuery;
import com.solarway.scheduleNotification.notificationService.core.application.command.CreateNotificationCommand;
import com.solarway.scheduleNotification.notificationService.core.domain.model.ScheduleNotification;
import com.solarway.scheduleNotification.notificationService.core.domain.shared.exception.ConflictException;

public class CreateNotificationUseCase<T extends NotificationQuery & NotificationMutation> {
    private final T mutation;
    private final int daysBefore;

    public CreateNotificationUseCase(T mutation, int daysBefore) {
        this.mutation = mutation;
        this.daysBefore = daysBefore;
    }

    public ScheduleNotification execute(CreateNotificationCommand command) {
        if(mutation.findByScheduleId(command.scheduleId()).isEmpty()) {
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

        throw new ConflictException("Notification already saved");
    }
}
