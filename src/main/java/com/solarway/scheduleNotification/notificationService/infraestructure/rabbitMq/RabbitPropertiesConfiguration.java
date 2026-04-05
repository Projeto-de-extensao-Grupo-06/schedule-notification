package com.solarway.scheduleNotification.notificationService.infraestructure.rabbitMq;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "broker")
public record RabbitPropertiesConfiguration(
        Exchange exchange,
        Queue createQueue,
        Queue updateQueue,
        Queue cancelQueue
) {
    public record Exchange(String name) {
    }

    public record Queue(String name) {
    }
}