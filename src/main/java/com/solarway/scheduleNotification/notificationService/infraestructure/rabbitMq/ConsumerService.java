package com.solarway.scheduleNotification.notificationService.infraestructure.rabbitMq;

import com.solarway.scheduleNotification.notificationService.core.application.command.CreateNotificationCommand;
import com.solarway.scheduleNotification.notificationService.core.application.usecase.CreateNotificationUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConsumerService {
    CreateNotificationUseCase createUserCase;

    @RabbitListener(queues = "${broker.queue.name}")
    public void createNotification(CreateNotificationCommand dto) {

    }
}