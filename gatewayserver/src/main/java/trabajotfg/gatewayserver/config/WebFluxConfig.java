package trabajotfg.gatewayserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.netty.http.server.HttpServer;

@Configuration
public class WebFluxConfig {

    @Bean
    public HttpServer httpServer() {
        return HttpServer.create()
                .httpRequestDecoder(spec -> spec.maxHeaderSize(16384)); // Aumenta a 16 KB
    }
}

