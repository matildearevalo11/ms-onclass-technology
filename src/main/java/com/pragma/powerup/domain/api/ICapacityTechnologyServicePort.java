package com.pragma.powerup.domain.api;

import com.pragma.powerup.domain.model.CapacityTechnologies;
import reactor.core.publisher.Mono;

public interface ICapacityTechnologyServicePort {
    Mono<Void> associate(CapacityTechnologies association);
}
