package com.solarway.scheduleNotification.notificationService.infraestructure.di;

import ch.qos.logback.classic.pattern.MessageConverter;
import com.solarway.scheduleNotification.notificationService.infraestructure.rabbitMq.RabbitPropertiesConfiguration;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(RabbitPropertiesConfiguration.class)
public class RabbitMqBeanConfig {
    private final RabbitPropertiesConfiguration properties;

    @Bean
    public Declarables rabbitDeclarables() {
        DirectExchange exchange = new DirectExchange(properties.exchange().name());

        Queue createQueue = QueueBuilder.durable(properties.createQueue().name()).build();
        Queue updateQueue = QueueBuilder.durable(properties.updateQueue().name()).build();
        Queue cancelQueue = QueueBuilder.durable(properties.cancelQueue().name()).build();

        Binding createBinding = BindingBuilder
                .bind(createQueue)
                .to(exchange)
                .with(properties.createQueue().name());

        Binding updateBinding = BindingBuilder
                .bind(updateQueue)
                .to(exchange)
                .with(properties.updateQueue().name());

        Binding cancelBinding = BindingBuilder
                .bind(cancelQueue)
                .to(exchange)
                .with(properties.cancelQueue().name());

        return new Declarables(
                exchange,
                createQueue, updateQueue, cancelQueue,
                createBinding, updateBinding, cancelBinding
        );
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
