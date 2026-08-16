package com.ecommerce.mel_ecom.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {


    @Bean
    public OpenAPI customOpenAPI() {
        SecurityScheme bearerScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .description("JWT Bearer Token");
        SecurityRequirement bearerRequirement = new SecurityRequirement()
                .addList("Bearer Authentication");

        return new OpenAPI()
                .info(new Info()
                        .title("Spring Boot E-Commerce REST API")
                        .version("1.0")
                        .description("This is an E-Commerce Project that uses Java, Spring Boot, PostgreSQL, AWS, Spring Security, Swagger, Postman, and React")
                        .license(new License().name("Apache 2.0").url("http://melecom.com"))
                        .contact(new Contact().name("Mohammad El-Tawil")
                                .email("mohammadeltawil0@gmail.com")
                                .url("https://github.com/mohammadeltawil0")))
                .externalDocs(new ExternalDocumentation()
                        .description("Project Docs")
                        .url("http://melecom.com"))
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication", bearerScheme))
                .addSecurityItem(bearerRequirement);
    }
}



