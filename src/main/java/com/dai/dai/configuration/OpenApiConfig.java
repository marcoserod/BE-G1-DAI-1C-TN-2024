package com.dai.dai.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.SwaggerUiConfigParameters;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new io.swagger.v3.oas.models.info.Info()
                        .title("BE-G1-DAI-1C-TN-2024")
                        .description("La API del backend de MoviePlay proporciona acceso a una plataforma de" +
                                " recomendación de películas revolucionaria. Permite a los usuarios registrarse" +
                                " utilizando Google Sign-In, explorar una amplia selección de películas, filtrarlas" +
                                " por género, buscar por título o actor, y acceder a detalles detallados de" +
                                " cada película. Además, ofrece funciones como calificación, guardar en favoritos" +
                                " y compartir películas, todo respaldado por un robusto manejo de errores y una" +
                                " documentación clara proporcionada mediante Swagger.")
                        .version("0.0.1"));
    }
}
