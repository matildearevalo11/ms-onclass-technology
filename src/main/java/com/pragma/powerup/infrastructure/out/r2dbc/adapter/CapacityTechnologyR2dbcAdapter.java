package com.pragma.powerup.infrastructure.out.r2dbc.adapter;

import com.pragma.powerup.domain.model.CapacityTechnologies;
import com.pragma.powerup.domain.spi.ICapacityTechnologyPersistencePort;
import com.pragma.powerup.infrastructure.out.r2dbc.entity.CapacityTechnologyEntity;
import com.pragma.powerup.infrastructure.out.r2dbc.repository.ICapacityTechnologyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class CapacityTechnologyR2dbcAdapter implements ICapacityTechnologyPersistencePort {
    private final ICapacityTechnologyRepository repository;

    @Override
    public Mono<Boolean> existsByCapacityId(Long capacityId) {
        return repository.existsByCapacityId(capacityId);
    }

    @Override
    public Mono<Void> save(CapacityTechnologies association) {
        return Flux.fromIterable(association.getTechnologyIds())
                .concatMap(id -> repository.save(new CapacityTechnologyEntity(null, association.getCapacityId(), id)))
                .then();
    }
}
