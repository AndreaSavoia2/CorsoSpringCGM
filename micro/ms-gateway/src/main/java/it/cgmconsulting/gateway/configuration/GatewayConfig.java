package it.cgmconsulting.gateway.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class GatewayConfig {

    private final AuthenticationFilter filter;

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("ms-auth", r -> r.path("/ms-auth/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://ms-auth"))
                .route("ms-post", r -> r.path("/ms-post/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://ms-post"))
                .route("ms-comment", r -> r.path("/ms-comment/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://ms-comment"))
                .route("ms-tag", r -> r.path("/ms-tag/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://ms-tag"))
                .route("ms-rating", r -> r.path("/ms-rating/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://ms-rating"))
                .build();
    }

}
