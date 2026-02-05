package com.services.investment_service.client;

import com.services.investment_service.dto.external.LandDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Feign Client for Land Service
 * Used to fetch land details from land-service
 */
@FeignClient(
        name = "land-service",
        url = "${services.land-service.url}",
        fallback = LandServiceClientFallback.class
)
public interface LandServiceClient {

    /**
     * Get land by ID
     * @param id Land ID
     * @return Land details including owner ID and location
     */
    @GetMapping("/api/lands/{id}")
    LandDTO getLandById(@PathVariable("id") Long id);
}