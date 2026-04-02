package com.solarway.scheduleNotification.notificationService.infraestructure.rabbitMq;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "broker")
public record RabbitPropertiesConfiguration(
        Exchange exchange,
        Queue queue
) {
    public record Exchange(String name) {
    }

    public record Queue(String name) {
    }
}