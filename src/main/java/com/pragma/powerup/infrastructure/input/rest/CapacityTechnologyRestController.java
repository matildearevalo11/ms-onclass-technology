package com.pragma.powerup.infrastructure.input.rest;

import com.pragma.powerup.application.dto.request.CapacityTechnologyRequestDto;
import com.pragma.powerup.application.handler.ICapacityTechnologyHandler;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/technologies/capacities")
@RequiredArgsConstructor
public class CapacityTechnologyRestController {
    private final ICapacityTechnologyHandler handler;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> associate(@Valid @RequestBody CapacityTechnologyRequestDto request) {
        return handler.associate(request);
    }
}
