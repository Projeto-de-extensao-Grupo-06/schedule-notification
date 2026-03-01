package com.solarway.scheduleNotification.notificationService.infraestructure.di;

import com.solarway.scheduleNotification.notificationService.core.adapters.NotificationSender;
import com.solarway.scheduleNotification.notificationService.core.application.usecase.CancelNotificationUseCase;
import com.solarway.scheduleNotification.notificationService.core.application.usecase.CreateNotificationUseCase;
import com.solarway.scheduleNotification.notificationService.core.application.usecase.SendPendingNotificationsUseCase;
import com.solarway.scheduleNotification.notificationService.core.application.usecase.UpdateNotificationUseCase;
import com.solarway.scheduleNotification.notificationService.infraestructure.persistence.jpa.ScheduleNotificationJpaAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class NotificationBeanConfig {
    @Value("${notification.days-before:2}")
    private int daysBefore;

    @Value("${spring.mail.username:}")
    private String mailFrom;

    @Bean
    public CreateNotificationUseCase createNotificationUseCase(ScheduleNotificationJpaAdapter adapter) {
        return new CreateNotificationUseCase(adapter, daysBefore);
    }

    @Bean
    public UpdateNotificationUseCase updateNotificationUseCase(ScheduleNotificationJpaAdapter adapter) {
        return new UpdateNotificationUseCase(adapter, adapter, daysBefore);
    }

    @Bean
    public CancelNotificationUseCase cancelNotificationUseCase(ScheduleNotificationJpaAdapter adapter) {
        return new CancelNotificationUseCase(adapter, adapter);
    }

    @Bean
    public SendPendingNotificationsUseCase sendPendingNotificationsUseCase(
            ScheduleNotificationJpaAdapter adapter,
            NotificationSender emailSender
    ) {
        return new SendPendingNotificationsUseCase(adapter, adapter, emailSender);
    }

}
