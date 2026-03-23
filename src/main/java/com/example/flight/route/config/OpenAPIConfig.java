package com.example.flight.route.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI myOpenAPI() {
        Server devServer = new Server();
        devServer.setUrl("http://localhost:8080");
        devServer.setDescription("Server URL in Development environment");

        Contact contact = new Contact();
        contact.setName("FlightRoute API");
        contact.setEmail("");

        Info info = new Info()
                .title("FlightRoute API Documentation")
                .version("1.0")
                .contact(contact)
                .description("This API exposes endpoints for FlightRoute application.");

        return new OpenAPI()
                .info(info)
                .servers(Collections.singletonList(devServer));
    }
} 