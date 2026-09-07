package com.notification.notification_service.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${notification.queues.email}")
    private String emailQueueName;

    @Value("${notification.queues.dlq}")
    private String emailDlqName;

    @Value("${notification.exchanges.main}")
    private String mainExchangeName;

    @Value("${notification.exchanges.dlx}")
    private String dlxExchangeName;

    @Value("${notification.routing-keys.email}")
    private String emailRoutingKey;

    @Value("${notification.routing-keys.dlq}")
    private String dlqRoutingKey;

    @Bean
    public DirectExchange mainExchange() {
        return new DirectExchange(mainExchangeName);
    }

    @Bean
    public DirectExchange deadLetterExchange() {
        return new DirectExchange(dlxExchangeName);
    }

    @Bean
    public Queue emailQueue() {
        return QueueBuilder.durable(emailQueueName)
                .withArgument("x-dead-letter-exchange", dlxExchangeName)
                .withArgument("x-dead-letter-routing-key", dlqRoutingKey)
                .build();
    }

    @Bean
    public Queue emailDeadLetterQueue() {
        return QueueBuilder.durable(emailDlqName).build();
    }

    @Bean
    public Binding emailBinding() {
        return BindingBuilder.bind(emailQueue())
                .to(mainExchange())
                .with(emailRoutingKey);
    }

    @Bean
    public Binding dlqBinding() {
        return BindingBuilder.bind(emailDeadLetterQueue())
                .to(deadLetterExchange())
                .with(dlqRoutingKey);
    }

    @Bean
    public JacksonJsonMessageConverter messageConverter() {
        return new JacksonJsonMessageConverter();
    }
}