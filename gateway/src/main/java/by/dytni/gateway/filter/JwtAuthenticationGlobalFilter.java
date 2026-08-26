package by.dytni.gateway.filter;


import static by.dytni.gateway.GatewayConstants.HttpHeader.AUTH_VALIDATE_URL;
import static by.dytni.gateway.GatewayConstants.HttpHeader.ROLE;
import static by.dytni.gateway.GatewayConstants.HttpHeader.TOKEN_QUERY;
import static by.dytni.gateway.GatewayConstants.HttpHeader.USER_ID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;

import by.dytni.gateway.service.JwtService;

import by.dytni.gateway.service.ReactiveUserStatusService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationGlobalFilter implements GlobalFilter, Ordered {

    private final WebClient.Builder webClientBuilder;
    private final JwtService jwtService;
    private final ReactiveUserStatusService userStatusService;

    @Value("${auth.service.url:http://localhost:8081}")
    private String authServiceUrl;

    @Value("#{'${gateway.public.paths}'.trim().split('\\s*,\\s*')}")
    private List<String> publicPaths;

    private WebClient webClient;

    @PostConstruct
    public void init() {
        this.webClient = webClientBuilder.baseUrl(authServiceUrl).build();
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();

        if (publicPaths.contains(path)) {
            log.debug("Public endpoint: {}, skipping JWT validation", path);
            return chain.filter(exchange);
        }

        String authorizationHeader = request.getHeaders().getFirst("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            log.warn("Missing or invalid Authorization header for {}", path);
            return onError(exchange, "Missing or invalid Authorization header", HttpStatus.UNAUTHORIZED);
        }

        String token = authorizationHeader.substring(7);
        if (!jwtService.validate(token)){
            log.warn("Token parsing failed for path {}", path);
            return onError(exchange, "Invalid or expired token", HttpStatus.UNAUTHORIZED);
        }
        Long id = jwtService.getUserId(token);
        String role = jwtService.getUserRole(token);

        return userStatusService.isActive(id)
                .flatMap(isActive -> {
                    if (!isActive) {
                        log.warn("User {} is blocked or inactive (Redis check)", id);
                        return onError(exchange, "User is inactive or blocked", HttpStatus.UNAUTHORIZED);
                    }
                    return proceedWithMutatedRequest(id, role, exchange, chain);
                })
                .onErrorResume(e -> {
                    log.warn("Redis check failed for user {}: {}. Falling back to auth-service...", id, e.getMessage());
                    return fallbackToAuthService(token, id, role, exchange, chain);
                });
    }

    private Mono<Void> fallbackToAuthService(String token, Long id, String role,
                                             ServerWebExchange exchange, GatewayFilterChain chain) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(AUTH_VALIDATE_URL)
                        .queryParam(TOKEN_QUERY, token)
                        .build())
                .header("Authorization", "Bearer " + token)
                .exchangeToMono(response -> {
                    if (response.statusCode().is2xxSuccessful()) {
                        log.debug("Fallback validation successful for user: {}", id);
                        return proceedWithMutatedRequest(id, role, exchange, chain);
                    } else if (response.statusCode().is4xxClientError()) {
                        log.warn("Fallback validation failed (4xx) for user: {}", id);
                        return onError(exchange, "Invalid token or user is blocked", HttpStatus.UNAUTHORIZED);
                    } else {
                        log.error("Auth service returned {} during fallback", response.statusCode());
                        return onError(exchange, "Authentication service error", HttpStatus.SERVICE_UNAVAILABLE);
                    }
                })
                .onErrorResume(e -> {
                    log.error("Failed to connect to auth-service during fallback for user {}: {}", id, e.getMessage());
                    return onError(exchange, "Authentication service unavailable", HttpStatus.SERVICE_UNAVAILABLE);
                });
    }

    private Mono<Void> proceedWithMutatedRequest(Long id, String role, ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                .headers(httpHeaders -> {
                    httpHeaders.remove(USER_ID);
                    httpHeaders.remove(ROLE);
                })
                .header(USER_ID, id.toString())
                .header(ROLE, role)
                .build();

        ServerWebExchange mutatedExchange = exchange.mutate()
                .request(mutatedRequest)
                .build();

        log.debug("Request mutated and forwarded for user: {}, role: {}", id, role);
        return chain.filter(mutatedExchange);
    }

    private Mono<Void> onError(ServerWebExchange exchange, String message, HttpStatus status) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(status);
        response.getHeaders().add("Content-Type", "application/json");

        String body = String.format(
                "{\"error\": \"%s\", \"status\": %d}", message, status.value()
        );

        byte[] bites = body.getBytes();
        return response.writeWith(Mono.just(response.bufferFactory().wrap(bites)));
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
