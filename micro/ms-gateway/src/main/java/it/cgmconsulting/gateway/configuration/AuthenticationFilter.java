package it.cgmconsulting.gateway.configuration;

import it.cgmconsulting.gateway.service.JWTService;
import it.cgmconsulting.gateway.service.JwtUser;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


@Component
@RequiredArgsConstructor
public class AuthenticationFilter implements GatewayFilter {

    private final RouteValidator routeValidator;
    private final JWTService jwtService;

    @Value("${application.security.internalToken}")
    String internalToken;
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain){
        ServerHttpRequest request = exchange.getRequest();

        // verifico se l'endpoint richiede il token
        if(routeValidator.isOpenEndPoint(request))
            exchange.getResponse().setStatusCode(HttpStatus.OK);
        else if (request.getHeaders().containsKey("Authorization-Internal") &&
                (request.getHeaders().getOrEmpty("Authorization-Internal").get(0).equals(internalToken))) {
            exchange.getResponse().setStatusCode(HttpStatus.OK);
        } else {
            // Se per la request è richiesto il token ma nell'header non compare la chiave 'Authorization', blocco tutto perchè significa che il token è mancante
            if(!isAuthMissing(request))
                return this.setCustomResponse(exchange, "Authorization header is missing in request", HttpStatus.UNAUTHORIZED);
            // estraggo il token dall'header
            String jwt = getJwtFromRequest(request);
            // se il token è null, blocco tutto
            if(jwt == null)
                return this.setCustomResponse(exchange, "Invalid token", HttpStatus.UNAUTHORIZED);
            // estraggo i claims dal token: se l'operazione non riesce viene sollevata un'eccezione e blocco tutto
            JwtUser jwtUser;
            try{
                jwtUser = jwtService.extractJwtUSer(jwt);
            } catch (Exception e) {
                return this.setCustomResponse(exchange, e.getMessage(), HttpStatus.UNAUTHORIZED);
            }

            if(
                    (jwtUser.getRole().contains("ADMIN") && request.getURI().getPath().contains("v1")) ||
                            (jwtUser.getRole().contains("WRITER") && request.getURI().getPath().contains("v2")) ||
                            (jwtUser.getRole().contains("MEMBER") && request.getURI().getPath().contains("v3")) ||
                            (jwtUser.getRole().contains("MODERATOR") && request.getURI().getPath().contains("v4")) ||
                            (jwtUser.getRole().contains("EDITORIAL_STAFF") && request.getURI().getPath().contains("v5"))
            )
                populateRequestWithNewHeader(exchange, jwtUser);
            else
                return this.setCustomResponse(exchange, "Invalid authorization", HttpStatus.UNAUTHORIZED);

        }
        return chain.filter(exchange);
    }

    private boolean isAuthMissing(ServerHttpRequest request){
        return request.getHeaders().containsKey("Authorization");
    }

    private String getJwtFromRequest(ServerHttpRequest request) {
        String bearerToken = request.getHeaders().getOrEmpty("Authorization").get(0);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private void populateRequestWithNewHeader(ServerWebExchange exchange, JwtUser jwtUser){
        exchange.getRequest().mutate()
                .header("userId", jwtUser.getId())
                .build();
    }

    private Mono<Void> setCustomResponse(ServerWebExchange exchange, String errorMsg, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        DataBuffer buffer = response.bufferFactory().wrap(errorMsg.getBytes());
        return response.writeWith(Mono.just(buffer));
    }
}

