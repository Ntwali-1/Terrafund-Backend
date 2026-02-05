package com.services.investment_service.client;

import com.services.investment_service.dto.external.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Feign Client for User Service
 * Used to fetch user details from user-service
 */
@FeignClient(
        name = "user-service",
        url = "${services.user-service.url}",
        fallback = UserServiceClientFallback.class
)
public interface UserServiceClient {

    /**
     * Get user by ID
     * @param id User ID
     * @return User details
     */
    @GetMapping("/api/users/{id}")
    UserDTO getUserById(@PathVariable("id") Long id);
}