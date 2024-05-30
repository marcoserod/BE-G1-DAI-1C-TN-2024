package com.dai.dai.configuration.swagger;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("MoviePlay API")
                        .version("2.0.7")
                        .description("The MoviePlay API offers services for managing movies and users, allowing " +
                                "users to access detailed information, search, filter, rate, and mark movies " +
                                "as favorites. Deployed in the cloud with Swagger documentation, it ensures a " +
                                "smooth experience and scalability. Developed with Spring Boot, " +
                                "it offers flexibility and seamlessly integrates with the MoviePlay " +
                                "mobile application"));
    }
}
