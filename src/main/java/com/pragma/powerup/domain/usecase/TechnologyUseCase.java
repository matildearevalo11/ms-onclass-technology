package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.api.ITechnologyServicePort;
import com.pragma.powerup.domain.exception.ConflictException;
import com.pragma.powerup.domain.exception.ExceptionMessages;
import com.pragma.powerup.domain.model.Technology;
import com.pragma.powerup.domain.spi.ITechnologyPersistencePort;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class TechnologyUseCase implements ITechnologyServicePort {

    private final ITechnologyPersistencePort persistencePort;

    @Override
    public Mono<Technology> createTechnology(Technology technology) {
        return Mono.defer(() -> persistencePort.existsByName(technology.name())
                .flatMap(exists -> exists
                        ? Mono.error(new ConflictException(ExceptionMessages.TECHNOLOGY_ALREADY_EXISTS.getMessage()))
                        : persistencePort.save(new Technology(null, technology.name(), technology.description()))));
    }
}
