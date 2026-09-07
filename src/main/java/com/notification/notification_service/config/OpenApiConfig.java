package com.notification.notification_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("Notificaiton Service API")
                .version("1.0.0")
                .description(
                        "asynchronous microservice for processing and dispatch of notification via RabbitMQ and Redis.")
                .contact(new Contact()
                        .name("Dev Team")
                        .email("dev@notification.com")));
    }
}
