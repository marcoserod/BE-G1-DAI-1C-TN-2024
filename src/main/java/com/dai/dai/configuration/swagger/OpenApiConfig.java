package com.dai.dai.configuration.swagger;

import org.springframework.beans.factory.annotation.Value;
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
                        .version("1.1.3")
                        .description("La API MoviePlay ofrece servicios para la gestión de películas y usuarios," +
                                " permitiendo a los usuarios acceder a información detallada, buscar, filtrar," +
                                " calificar y marcar películas como favoritas. Desplegada en la nube con " +
                                "documentación Swagger, garantiza una experiencia fluida y escalabilidad." +
                                " Desarrollada con Node.js/Express o Spring Boot, ofrece flexibilidad y se integra" +
                                " perfectamente con la aplicación móvil MoviePlay."));
    }
}
