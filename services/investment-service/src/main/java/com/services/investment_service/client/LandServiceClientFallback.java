package com.services.investment_service.client;

import com.services.investment_service.dto.external.LandDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Fallback implementation for LandServiceClient
 * Used when land-service is unavailable or request fails
 */
@Component
@Slf4j
public class LandServiceClientFallback implements LandServiceClient {

    @Override
    public LandDTO getLandById(Long id) {
        log.error("Fallback triggered: Unable to fetch land with ID: {} from land-service", id);
        log.error("Land service might be down or unreachable");

        // Return null - let the calling service handle this appropriately
        // Alternative: throw a custom exception
        return null;
    }
}