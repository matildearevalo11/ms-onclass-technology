package com.pragma.powerup.infrastructure.input.rest;

import com.pragma.powerup.application.dto.request.TechnologyRequestDto;
import com.pragma.powerup.application.handler.ITechnologyHandler;
import com.pragma.powerup.infrastructure.input.rest.support.RequestValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class TechnologyRestHandler {
    private final ITechnologyHandler handler;
    private final RequestValidator validator;

    public Mono<ServerResponse> createTechnology(ServerRequest request) {
        return validator.body(request, TechnologyRequestDto.class)
                .flatMap(handler::createTechnology)
                .flatMap(technology -> ServerResponse.status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON).bodyValue(technology));
    }
}
