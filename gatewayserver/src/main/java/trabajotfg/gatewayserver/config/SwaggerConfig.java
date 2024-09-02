package trabajotfg.gatewayserver.config;

import org.springdoc.core.properties.AbstractSwaggerUiConfigProperties.SwaggerUrl;
import org.springdoc.core.properties.SwaggerUiConfigParameters;
import org.springframework.boot.CommandLineRunner;
import java.lang.System;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

import static org.springdoc.core.utils.Constants.DEFAULT_API_DOCS_URL;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Configuration
public class SwaggerConfig {


   
   

    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("RentaMov")
                        .description("API para el Trabajo de Fin de Grado")
                        .version("1.0")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")));
    }

    
    }

