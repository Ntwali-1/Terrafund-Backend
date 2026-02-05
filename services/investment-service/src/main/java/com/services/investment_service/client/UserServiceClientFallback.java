package com.services.investment_service.client;

import com.services.investment_service.dto.external.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserServiceClientFallback implements UserServiceClient {

    @Override
    public UserDTO getUserById(Long id) {
        log.error("Fallback triggered: Unable to fetch user with ID: {} from user-service", id);
        log.error("User service might be down or unreachable");

        // Return null - let the calling service handle this appropriately
        // Alternative: throw a custom exception
        return null;
    }
}