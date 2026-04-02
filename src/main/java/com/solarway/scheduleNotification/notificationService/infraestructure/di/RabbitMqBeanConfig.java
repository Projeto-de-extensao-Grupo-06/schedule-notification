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
        FanoutExchange exchange = new FanoutExchange(properties.exchange().name());

        Queue queue = QueueBuilder
                .durable(properties.queue().name())
                .build();

        Binding binding = BindingBuilder
                .bind(queue)
                .to(exchange);

        return new Declarables(exchange, queue, binding);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
