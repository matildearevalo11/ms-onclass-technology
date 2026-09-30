package com.pragma.powerup.infrastructure.out.r2dbc.repository;

import com.pragma.powerup.infrastructure.out.r2dbc.entity.CapacityTechnologyEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface ICapacityTechnologyRepository extends ReactiveCrudRepository<CapacityTechnologyEntity, Long> {
    Mono<Boolean> existsByCapacityId(Long capacityId);
}
