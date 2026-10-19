package com.pragma.powerup.infrastructure.input.rest;

import com.pragma.powerup.application.dto.request.CapacityTechnologyListRequestDto;
import com.pragma.powerup.application.dto.request.CapacityTechnologyRequestDto;
import com.pragma.powerup.application.dto.response.CapacityTechnologyListResponseDto;
import com.pragma.powerup.application.handler.ICapacityTechnologyDeleteHandler;
import com.pragma.powerup.application.handler.ICapacityTechnologyHandler;
import com.pragma.powerup.application.handler.ICapacityTechnologyListHandler;
import com.pragma.powerup.infrastructure.constants.ApiParameters;
import com.pragma.powerup.infrastructure.input.rest.support.QueryParams;
import com.pragma.powerup.infrastructure.input.rest.support.RequestValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class CapacityTechnologyRestHandler {
    private final ICapacityTechnologyHandler handler;
    private final ICapacityTechnologyListHandler listHandler;
    private final ICapacityTechnologyDeleteHandler deleteHandler;
    private final RequestValidator validator;

    public Mono<ServerResponse> associate(ServerRequest request) {
        return validator.body(request, CapacityTechnologyRequestDto.class)
                .flatMap(handler::associate)
                .then(ServerResponse.noContent().build());
    }

    public Mono<ServerResponse> listByCapacityIds(ServerRequest request) {
        return capacityIds(request)
                .flatMap(query -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                        .body(listHandler.listByCapacityIds(query), CapacityTechnologyListResponseDto.class));
    }

    public Mono<ServerResponse> deleteForCapacities(ServerRequest request) {
        return capacityIds(request)
                .flatMap(query -> deleteHandler.deleteForCapacities(query.getCapacityIds()))
                .then(ServerResponse.noContent().build());
    }

    private Mono<CapacityTechnologyListRequestDto> capacityIds(ServerRequest request) {
        return Mono.fromCallable(() -> {
            var query = new CapacityTechnologyListRequestDto();
            query.setCapacityIds(QueryParams.longList(request, ApiParameters.CAPACITY_IDS));
            return query;
        }).flatMap(validator::validate);
    }
}
