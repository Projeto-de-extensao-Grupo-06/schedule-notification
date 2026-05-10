package com.solarway.scheduleNotification.notificationService.core.application.usecase;

import com.solarway.scheduleNotification.notificationService.core.adapters.NotificationMutation;
import com.solarway.scheduleNotification.notificationService.core.adapters.NotificationQuery;
import com.solarway.scheduleNotification.notificationService.core.application.command.UpdateNotificationCommand;
import com.solarway.scheduleNotification.notificationService.core.domain.model.ScheduleNotification;
import com.solarway.scheduleNotification.notificationService.core.domain.shared.exception.NotificationNotFoundException;
import com.solarway.scheduleNotification.notificationService.core.domain.shared.vo.Contato;

import java.util.List;

public class UpdateNotificationUseCase {
    private final NotificationQuery query;
    private final NotificationMutation mutation;
    private final int daysBefore;

    public UpdateNotificationUseCase(NotificationQuery query, NotificationMutation mutation, int daysBefore) {
        this.query = query;
        this.mutation = mutation;
        this.daysBefore = daysBefore;
    }
    public ScheduleNotification execute(UpdateNotificationCommand command) {
        ScheduleNotification existing = query.findByScheduleId(command.scheduleId())
                .orElseThrow(() -> new NotificationNotFoundException(command.scheduleId()));

        List<Contato> recipients = command.recipients().stream()
                .map(recipient -> Contato.of(recipient.email(), recipient.phone()))
                .toList();

        ScheduleNotification updated = ScheduleNotification.existing(
                existing.getId(),
                command.scheduleId(),
                command.projectTitle(),
                command.title(),
                recipients,
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
