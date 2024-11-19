package trabajotfg.gatewayserver.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.Contact;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "RentaMov API",
        version = "1.0",
        description = "API general para la gestión de inventario, reservas, pagos y localización en el sistema de alquiler de vehículos.",
        contact = @Contact(
            name = "Alberto Ávila Fernández",
            email = "albertoaf0520@gmail.com",
            url = "https://www.rentamov.com"
        )
    )
)
public class OpenApiGlobalConfiguration {
}