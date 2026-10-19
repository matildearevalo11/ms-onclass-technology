package com.pragma.powerup.infrastructure.input.rest;

import com.pragma.powerup.infrastructure.constants.ApiPaths;
import com.pragma.powerup.infrastructure.exceptionhandler.ErrorHandlingFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class CapacityTechnologyRouter {
    @Bean
    RouterFunction<ServerResponse> capacityTechnologyRoutes(CapacityTechnologyRestHandler handler,
            ErrorHandlingFilter errors) {
        return RouterFunctions.route()
                .POST(ApiPaths.CAPACITY_TECHNOLOGIES, handler::associate)
                .GET(ApiPaths.CAPACITY_TECHNOLOGIES, handler::listByCapacityIds)
                .DELETE(ApiPaths.CAPACITY_TECHNOLOGIES, handler::deleteForCapacities)
                .filter(errors)
                .build();
    }
}
