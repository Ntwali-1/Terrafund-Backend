package com.services.gateway.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public List<GroupedOpenApi> apis(RouteDefinitionLocator locator) {
        List<GroupedOpenApi> groups = new ArrayList<>();
        List<RouteDefinition> definitions = locator.getRouteDefinitions().collectList().block();
        
        if (definitions != null) {
            definitions.stream()
                .filter(routeDefinition -> routeDefinition.getId().matches(".*-service$"))
                .forEach(routeDefinition -> {
                    String name = routeDefinition.getId().replaceAll("-service$", "");
                    groups.add(GroupedOpenApi.builder()
                        .group(name)
                        .pathsToMatch("/" + name + "/**")
                        .build());
                });
        }
        
        return groups;
    }
}
