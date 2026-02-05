package com.services.investment_service.client;

import com.services.investment_service.dto.external.LandDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "land-service",
        fallback = LandServiceClientFallback.class
)
public interface LandServiceClient {

    @GetMapping("/api/lands/{id}")
    LandDTO getLandById(@PathVariable("id") Long id);
}