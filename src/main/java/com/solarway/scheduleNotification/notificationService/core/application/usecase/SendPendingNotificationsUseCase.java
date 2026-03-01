package com.solarway.scheduleNotification.notificationService.core.application.usecase;

import com.solarway.scheduleNotification.notificationService.core.adapters.NotificationMutation;
import com.solarway.scheduleNotification.notificationService.core.adapters.NotificationQuery;
import com.solarway.scheduleNotification.notificationService.core.adapters.NotificationSender;
import com.solarway.scheduleNotification.notificationService.core.domain.model.ScheduleNotification;

import java.util.List;

public class SendPendingNotificationsUseCase {
    private final NotificationQuery query;
    private final NotificationMutation mutation;
    private final NotificationSender sender;

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
                notification.markAsFailed();
            }
            mutation.save(notification);
        }
        }
    }


