package com.pragma.powerup.infrastructure.input.rest;

import com.pragma.powerup.application.dto.request.CapacityTechnologyRequestDto;
import com.pragma.powerup.application.handler.ICapacityTechnologyHandler;
import com.pragma.powerup.application.handler.ICapacityTechnologyListHandler;
import com.pragma.powerup.application.handler.ICapacityTechnologyDeleteHandler;
import com.pragma.powerup.application.dto.request.CapacityTechnologyListRequestDto;
import com.pragma.powerup.application.dto.response.CapacityTechnologyListResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/technologies/capacities")
@RequiredArgsConstructor
public class CapacityTechnologyRestController {
    private final ICapacityTechnologyHandler handler;
    private final ICapacityTechnologyListHandler listHandler;
    private final ICapacityTechnologyDeleteHandler deleteHandler;

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteForCapacities(@Valid @ModelAttribute CapacityTechnologyListRequestDto request) {
        return deleteHandler.deleteForCapacities(request.getCapacityIds());
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<CapacityTechnologyListResponseDto> listByCapacityIds(
            @Valid @ModelAttribute CapacityTechnologyListRequestDto request) {
        return listHandler.listByCapacityIds(request);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> associate(@Valid @RequestBody CapacityTechnologyRequestDto request) {
        return handler.associate(request);
    }
}
