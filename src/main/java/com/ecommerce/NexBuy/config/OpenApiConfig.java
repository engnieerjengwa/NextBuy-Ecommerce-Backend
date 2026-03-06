package com.ecommerce.NexBuy.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI nexbuyOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("NexBuy E-Commerce API")
                        .description("REST API for NexBuy — Zimbabwe's online shopping platform. "
                                + "Covers product catalog, authentication, orders, payments, "
                                + "deals, loyalty, gift cards, and more.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("NexBuy Team")
                                .email("dev@nexbuy.co.zw"))
                        .license(new License()
                                .name("Proprietary")))
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Development"),
                        new Server().url("https://api.nexbuy.com").description("Production")))
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Enter JWT token obtained from /api/auth/login")));
    }
}
