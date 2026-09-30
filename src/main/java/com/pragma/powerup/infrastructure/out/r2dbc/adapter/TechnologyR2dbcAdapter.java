package com.pragma.powerup.infrastructure.out.r2dbc.adapter;

import java.util.List;
import com.pragma.powerup.domain.exception.ConflictException;
import com.pragma.powerup.domain.exception.ExceptionMessages;
import com.pragma.powerup.domain.model.Technology;
import com.pragma.powerup.domain.spi.ITechnologyPersistencePort;
import com.pragma.powerup.infrastructure.out.r2dbc.mapper.TechnologyEntityMapper;
import com.pragma.powerup.infrastructure.out.r2dbc.repository.ITechnologyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class TechnologyR2dbcAdapter implements ITechnologyPersistencePort {

    private final ITechnologyRepository repository;
    private final TechnologyEntityMapper mapper;

    @Override
    public Mono<Long> countByIds(List<Long> ids) {
        return repository.countByIdIn(ids);
    }

    @Override
    public Mono<Boolean> existsByName(String name) {
        return repository.existsByName(name);
    }

    @Override
    public Mono<Technology> save(Technology technology) {
        return repository.save(mapper.toEntity(technology))
                .map(mapper::toModel)
                .onErrorMap(DuplicateKeyException.class, exception -> new ConflictException(ExceptionMessages.TECHNOLOGY_ALREADY_EXISTS.getMessage()));
    }
}
