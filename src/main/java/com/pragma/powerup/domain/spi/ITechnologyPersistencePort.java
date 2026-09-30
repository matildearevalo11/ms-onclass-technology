package com.pragma.powerup.domain.spi;

import com.pragma.powerup.domain.model.Technology;
import reactor.core.publisher.Mono;

public interface ITechnologyPersistencePort {
    Mono<Boolean> existsByName(String name);

    Mono<Technology> save(Technology technology);
}
