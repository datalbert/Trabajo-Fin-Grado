package trabajotfg.gatewayserver.config;


import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;

import org.springframework.security.web.server.SecurityWebFilterChain;




@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) 
     {
        http
        //.csrf().disable()
        
         .cors(cors->cors
             .configurationSource(exchanges-> {
                org.springframework.web.cors.CorsConfiguration corsConfiguration = new org.springframework.web.cors.CorsConfiguration();
                corsConfiguration.addAllowedOrigin("http://localhost:4200");
                corsConfiguration.addAllowedMethod("*");
                corsConfiguration.addAllowedHeader("*");
                corsConfiguration.setAllowCredentials(true);
                corsConfiguration.addExposedHeader("Authorization");
                return corsConfiguration;
             })
            )
         
            
        
            .authorizeExchange(exchanges -> exchanges
                
                 //permitimos las rutas hacia la documentación de swagger
                 .pathMatchers("/tfg/inventario/v3/api-docs").permitAll()
                .pathMatchers("/tfg/reservas/v3/api-docs").permitAll()
                .pathMatchers("/tfg/pagos/v3/api-docs").permitAll()
                .pathMatchers("/tfg/gps/v3/api-docs").permitAll()
                
                .pathMatchers("/tfg/usuarios/**").authenticated()
                .pathMatchers("/tfg/inventario/**").authenticated()
                .pathMatchers("/tfg/reservas/**").authenticated()
                .pathMatchers("/tfg/pagos/**").authenticated()
                .pathMatchers("/tfg/gps/**").authenticated()
                .pathMatchers("/tfg/inventario/nuevoCoche").hasRole("Provedor")
                .pathMatchers("/tfg/inventario/actualizarCoche").hasRole("Provedor")
                .pathMatchers("/tfg/inventario/eliminarCoche/*").hasRole("Provedor")
                .anyExchange().permitAll())
                .oauth2ResourceServer(oAuth2ResourceServerSpec -> oAuth2ResourceServerSpec
                .jwt(jwtSpec -> jwtSpec.jwtAuthenticationConverter(grantedAuthoritiesExtractor())));
                
        
        
        return http.build();
    }

     private Converter<Jwt, Mono<AbstractAuthenticationToken>> grantedAuthoritiesExtractor() {
        JwtAuthenticationConverter jwtAuthenticationConverter =
                new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new KeycloakRoleconverter());
        return new ReactiveJwtAuthenticationConverterAdapter(jwtAuthenticationConverter);
    }


     

    


   




}
