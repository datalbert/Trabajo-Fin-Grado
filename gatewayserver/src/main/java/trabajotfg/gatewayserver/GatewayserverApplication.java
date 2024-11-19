package trabajotfg.gatewayserver;

import org.springdoc.core.properties.AbstractSwaggerUiConfigProperties.SwaggerUrl;
import org.springdoc.core.properties.SwaggerUiConfigParameters;
import org.springdoc.core.properties.SwaggerUiConfigProperties;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;

import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;

import static org.springdoc.core.utils.Constants.DEFAULT_API_DOCS_URL;

import java.util.*;
import java.util.stream.Collectors;

@SpringBootApplication
public class GatewayserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayserverApplication.class, args);
	}

	

	@Bean
	public RouteLocator tfgRouteLocatio(RouteLocatorBuilder builder) {
		RouteLocator routeLocator = builder.routes()
				.route("inventario-service",p -> p
						.path("/tfg/inventario/**")
						.filters(f -> f.rewritePath("/tfg/inventario/(?<segment>.*)", "/${segment}"))
						.uri("lb://INVENTARIO"))
				.route("reservas-service", p -> p
						.path("/tfg/reservas/**")
						.filters(f -> f.rewritePath("/tfg/reservas/(?<segment>.*)", "/${segment}"))
						.uri("lb://RESERVAS"))
				.route("pago-service", p -> p
						.path("/tfg/pagos/**")
						.filters(f -> f.rewritePath("/tfg/pagos/(?<segment>.*)", "/${segment}"))
						.uri("lb://PAGO"))
				.route("gps-service", p -> p
						.path("/tfg/gps/**")
						.filters(f -> f.rewritePath("/tfg/gps/(?<segment>.*)", "/${segment}"))
						.uri("lb://GPS"))
				.build();

		// Imprimir las rutas definidas
		System.out.println("Rutas definidas: " + routeLocator.getRoutes().collectList().block());

		return routeLocator;
	}

	@Bean
	@Lazy(false)
	public Set<SwaggerUrl> apis(RouteLocator routeLocator, SwaggerUiConfigProperties swaggerUiConfigProperties) {
		Set<SwaggerUrl> urls = new HashSet<>();
		routeLocator.getRoutes().toStream()
			.filter(route -> route.getId().matches(".*-service"))
			.forEach(route -> {
				String name = route.getId().replaceAll("-service", "");
				SwaggerUrl swaggerUrl = new SwaggerUrl(name, "/tfg/" + name + "/v3/api-docs", null);
				urls.add(swaggerUrl);
			});
		swaggerUiConfigProperties.setUrls(urls);
		return urls;
	}



	

	


}
