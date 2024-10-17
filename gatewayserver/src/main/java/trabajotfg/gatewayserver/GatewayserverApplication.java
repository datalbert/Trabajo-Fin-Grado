package trabajotfg.gatewayserver;

import org.springdoc.core.properties.AbstractSwaggerUiConfigProperties.SwaggerUrl;
import org.springdoc.core.properties.SwaggerUiConfigParameters;
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
				.route("usuarios-service", p -> p
						.path("/tfg/usuarios/**")
						.filters(f -> f.rewritePath("/tfg/usuarios/(?<segment>.*)", "/${segment}"))
						.uri("lb://USUARIOS"))
				.route(p -> p
						.path("/tfg/inventario/**")
						.filters(f -> f.rewritePath("/tfg/inventario/(?<segment>.*)", "/${segment}"))
						.uri("lb://INVENTARIO"))
				.route("reservas-service", p -> p
						.path("/tfg/reservas/**")
						.filters(f -> f.rewritePath("/tfg/reservas/(?<segment>.*)", "/${segment}"))
						.uri("lb://RESERVAS"))
				.route("pagos-service", p -> p
						.path("/tfg/pagos/**")
						.filters(f -> f.rewritePath("/tfg/pagos/(?<segment>.*)", "/${segment}"))
						.uri("lb://PAGOS"))
				.route("gps-service", p -> p
						.path("/tfg/gps/**")
						.filters(f -> f.rewritePath("/tfg/gps/(?<segment>.*)", "/${segment}"))
						.uri("lb://GPS"))
				//rutas para swagger
				.route("swagger-usuario", p -> p
						.path("/v3/api-docs/usuarios")
						.filters(f -> f.rewritePath("/v3/api-docs/(?<segment>.*)", "/v3/api-docs/${segment}"))
						.uri("lb://USUARIOS"))
				.route("swagger-inventario", p -> p
						.path("/v3/api-docs/inventario")
						.filters(f -> f.rewritePath("/v3/api-docs/(?<segment>.*)", "/v3/api-docs"))
						.uri("lb://INVENTARIO"))
				.route("swagger-reservas", p -> p
						.path("/v3/api-docs/reservas")
						.filters(f -> f.rewritePath("/v3/api-docs/(?<segment>.*)", "v3/api-docs"))
						.uri("lb://RESERVAS"))
				.build();

		// Imprimir las rutas definidas
		System.out.println("Rutas definidas: " + routeLocator.getRoutes().collectList().block());

		return routeLocator;
	}

	@Bean
	@Lazy(false)
	@DependsOn("tfgRouteLocatio")
	public Set<SwaggerUrl> apis(RouteDefinitionLocator locator, SwaggerUiConfigParameters swaggerUiConfigParameters) {
		Set<SwaggerUrl> urls = new HashSet<>();
		List<RouteDefinition> definitions = locator.getRouteDefinitions().collectList().block();
		for (RouteDefinition routeDefinition : definitions) {
			System.out.println("RouteDefinition: " + routeDefinition);
		}
		definitions.stream().filter(routeDefinition -> routeDefinition.getId().matches(".*-service")).forEach(routeDefinition -> {
			String name = routeDefinition.getId().replaceAll("-service", "");
			SwaggerUrl swaggerUrl = new SwaggerUrl(name, DEFAULT_API_DOCS_URL+"/" + name, null);
			urls.add(swaggerUrl);
		});
		swaggerUiConfigParameters.setUrls(urls);
		return urls;
	}



	

	


}
