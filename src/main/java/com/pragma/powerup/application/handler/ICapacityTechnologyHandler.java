package com.pragma.powerup.application.handler;

import com.pragma.powerup.application.dto.request.CapacityTechnologyRequestDto;
import reactor.core.publisher.Mono;

public interface ICapacityTechnologyHandler {
    Mono<Void> associate(CapacityTechnologyRequestDto request);
}
