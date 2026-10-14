package com.pragma.powerup.infrastructure.out.r2dbc.repository;

import java.util.List;
import com.pragma.powerup.infrastructure.out.r2dbc.entity.TechnologyEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface ITechnologyRepository extends ReactiveCrudRepository<TechnologyEntity, Long> {
    Mono<Long> countByIdIn(List<Long> ids);

    Mono<Boolean> existsByName(String name);
}
