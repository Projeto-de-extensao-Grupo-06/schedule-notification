package com.solarway.scheduleNotification.notificationService.infraestructure.scheduler;

import com.solarway.scheduleNotification.notificationService.core.application.usecase.SendPendingNotificationsUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class NotificationSchedulerJob {
      private static final Logger log = LoggerFactory.getLogger(NotificationSchedulerJob.class);

      private final SendPendingNotificationsUseCase sendPendingUseCase;

    public NotificationSchedulerJob(SendPendingNotificationsUseCase sendPendingUseCase) {
        this.sendPendingUseCase = sendPendingUseCase;
    }

    @Scheduled(fixedDelayString = "${notification.scheduler.job-interval-ms:60000}")
    public void run() {
        log.info("Chegando notificações pendentes...");
        sendPendingUseCase.execute();
        log.info("Chegagem finalizado.");
    }
}
