package com.pragma.powerup.domain.spi;

import com.pragma.powerup.domain.model.CapacityTechnologies;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;
import java.util.List;

public interface ICapacityTechnologyPersistencePort {
    Mono<Boolean> existsByCapacityId(Long capacityId);
    Mono<Void> save(CapacityTechnologies association);
    Flux<Long> findTechnologyIdsByCapacityIds(List<Long> capacityIds);
    Mono<Void> deleteByCapacityIds(List<Long> capacityIds);
}
