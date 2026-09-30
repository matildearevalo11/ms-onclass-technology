package com.pragma.powerup.domain.spi;

import com.pragma.powerup.domain.model.CapacityTechnologies;
import reactor.core.publisher.Mono;

public interface ICapacityTechnologyPersistencePort {
    Mono<Boolean> existsByCapacityId(Long capacityId);
    Mono<Void> save(CapacityTechnologies association);
}
