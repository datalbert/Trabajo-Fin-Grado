package trabajotfg.gatewayserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GatewayserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayserverApplication.class, args);
	}

	@Bean
	public RouteLocator tfgRouteLocatio(RouteLocatorBuilder builder){

		return builder.routes()
				.route(p -> p
						.path("/tfg/usuarios/**")
						.filters(f -> f.rewritePath("/tfg/usuarios/(?<segment>.*)","/${segment}"))
						.uri("lb://USUARIOS"))
				.route(p -> p
						.path("/tfg/inventario/**")
						.uri("lb://USUARIOS"))
				.route(p -> p
						.path("/tfg/reservas/**")
						.uri("lb://USUARIOS"))
				.build();


	}

}
