package com.pragma.powerup.domain.spi;

import java.util.List;
import com.pragma.powerup.domain.model.Technology;
import reactor.core.publisher.Mono;

public interface ITechnologyPersistencePort {
    Mono<Long> countByIds(List<Long> ids);
    Mono<Boolean> existsByName(String name);

    Mono<Technology> save(Technology technology);
    Mono<Void> deleteUnreferencedByIds(List<Long> ids);
}
