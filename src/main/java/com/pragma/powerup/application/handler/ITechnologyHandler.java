package com.pragma.powerup.application.handler;

import com.pragma.powerup.application.dto.request.TechnologyRequestDto;
import com.pragma.powerup.application.dto.response.TechnologyResponseDto;
import reactor.core.publisher.Mono;

public interface ITechnologyHandler {
    Mono<TechnologyResponseDto> createTechnology(TechnologyRequestDto request);
}
