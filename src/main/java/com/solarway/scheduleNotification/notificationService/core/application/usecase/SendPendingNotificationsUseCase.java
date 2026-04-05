package com.solarway.scheduleNotification.notificationService.core.application.usecase;

import com.solarway.scheduleNotification.notificationService.core.adapters.NotificationMutation;
import com.solarway.scheduleNotification.notificationService.core.adapters.NotificationQuery;
import com.solarway.scheduleNotification.notificationService.core.adapters.NotificationSender;
import com.solarway.scheduleNotification.notificationService.core.domain.model.ScheduleNotification;
import com.solarway.scheduleNotification.notificationService.infraestructure.scheduler.NotificationSchedulerJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SendPendingNotificationsUseCase {
    private final NotificationQuery query;
    private final NotificationMutation mutation;
    private final NotificationSender sender;
    private static final Logger log = LoggerFactory.getLogger(SendPendingNotificationsUseCase.class);


    public SendPendingNotificationsUseCase(
          NotificationQuery query,
          NotificationMutation mutation,
          NotificationSender sender
    ){
        this.query = query;
        this.mutation = mutation;
        this.sender = sender;
    }

    public void execute(){
        List<ScheduleNotification> pending = query.findAllPendingDue();

        for (ScheduleNotification notification : pending) {
            try {
                sender.send(notification);
                notification.markAsSent();
            } catch (Exception e) {
                log.error("Erro ao enviar notificação {}: {}", notification.getScheduleId(), e.getMessage());
                notification.markAsFailed();
            }
            mutation.save(notification);
        }
        }
    }


