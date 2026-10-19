package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.api.ICapacityTechnologyServicePort;
import com.pragma.powerup.domain.exception.ConflictException;
import com.pragma.powerup.domain.exception.ExceptionMessages;
import com.pragma.powerup.domain.exception.NotFoundException;
import com.pragma.powerup.domain.model.CapacityTechnologies;
import com.pragma.powerup.domain.spi.ICapacityTechnologyPersistencePort;
import com.pragma.powerup.domain.spi.ITechnologyPersistencePort;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class CapacityTechnologyUseCase implements ICapacityTechnologyServicePort {
    private final ITechnologyPersistencePort technologyPort;
    private final ICapacityTechnologyPersistencePort associationPort;

    @Override
    public Mono<Void> associate(CapacityTechnologies association) {
        return associationPort.existsByCapacityId(association.getCapacityId())
                .flatMap(exists -> Boolean.TRUE.equals(exists)
                        ? Mono.error(new ConflictException(ExceptionMessages.TECHNOLOGIES_ALREADY_ASSOCIATED.getMessage()))
                        : technologyPort.countByIds(association.getTechnologyIds()))
                .flatMap(count -> count == association.getTechnologyIds().size()
                        ? associationPort.save(association)
                        : Mono.error(new NotFoundException(ExceptionMessages.TECHNOLOGIES_NOT_FOUND.getMessage())));
    }
}
