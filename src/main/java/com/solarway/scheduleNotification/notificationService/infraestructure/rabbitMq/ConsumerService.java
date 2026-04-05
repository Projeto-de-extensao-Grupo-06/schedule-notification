package com.solarway.scheduleNotification.notificationService.infraestructure.rabbitMq;

import com.solarway.scheduleNotification.notificationService.core.application.command.CancelNotificationCommand;
import com.solarway.scheduleNotification.notificationService.core.application.command.CreateNotificationCommand;
import com.solarway.scheduleNotification.notificationService.core.application.command.UpdateNotificationCommand;
import com.solarway.scheduleNotification.notificationService.core.application.usecase.CancelNotificationUseCase;
import com.solarway.scheduleNotification.notificationService.core.application.usecase.CreateNotificationUseCase;
import com.solarway.scheduleNotification.notificationService.core.application.usecase.UpdateNotificationUseCase;
import com.solarway.scheduleNotification.notificationService.core.domain.shared.exception.ConflictException;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConsumerService {
    private final CreateNotificationUseCase createNotificationUseCase;
    private final UpdateNotificationUseCase updateNotificationUseCase;
    private final CancelNotificationUseCase cancelNotificationUseCase;

    @RabbitListener(queues = "${broker.create-queue.name}")
    public void createNotification(CreateNotificationCommand dto) {
        try {
            createNotificationUseCase.execute(dto);
        } catch (ConflictException e) {
            System.out.println(e.getMessage());
        }
    }

    @RabbitListener(queues = "${broker.update-queue.name}")
    public void updateNotification(UpdateNotificationCommand dto) {
        updateNotificationUseCase.execute(dto);
    }

    @RabbitListener(queues = "${broker.cancel-queue.name}")
    public void cancelNotification(CancelNotificationCommand dto) {
        cancelNotificationUseCase.execute(dto);
    }
}