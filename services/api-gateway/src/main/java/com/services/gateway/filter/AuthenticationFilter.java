package com.services.gateway.filter;

import com.services.gateway.config.RouteValidator;
import com.services.gateway.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    @Autowired
    private RouteValidator validator;

    @Autowired
    private JwtUtil jwtUtil;

    public AuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            
            // Log the request path for debugging
            System.out.println("Processing request: " + request.getURI().getPath());
            
            if (validator.isSecured.test(request)) {
                // Check for authorization header
                if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    System.out.println("Missing authorization header for: " + request.getURI().getPath());
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing authorization header");
                }

                String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    authHeader = authHeader.substring(7);
                } else {
                    System.out.println("Invalid authorization header format");
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid authorization header format");
                }

                try {
                    // Validate token
                    jwtUtil.validateToken(authHeader);
                    System.out.println("Token validated successfully");
                } catch (Exception e) {
                    System.out.println("Token validation failed: " + e.getMessage());
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid access token");
                }
            } else {
                System.out.println("Skipping authentication for: " + request.getURI().getPath());
            }
            return chain.filter(exchange);
        });
    }

    public static class Config {

    }
}
