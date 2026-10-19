package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.api.ICapacityTechnologyServicePort;
import com.pragma.powerup.domain.exception.ConflictException;
import com.pragma.powerup.domain.exception.ExceptionMessages;
import com.pragma.powerup.domain.exception.NotFoundException;
import com.pragma.powerup.domain.model.CapacityTechnologies;
import com.pragma.powerup.domain.spi.ICapacityTechnologyPersistencePort;
import com.pragma.powerup.domain.spi.ITechnologyPersistencePort;
import com.pragma.powerup.domain.util.ApplicationConstants;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import static com.pragma.powerup.domain.util.DomainValidations.requirePositive;
import static com.pragma.powerup.domain.util.DomainValidations.requirePositiveIds;
import static com.pragma.powerup.domain.util.DomainValidations.requireSizeBetween;
import static com.pragma.powerup.domain.util.DomainValidations.requireUnique;

@RequiredArgsConstructor
public class CapacityTechnologyUseCase implements ICapacityTechnologyServicePort {
    private final ITechnologyPersistencePort technologyPort;
    private final ICapacityTechnologyPersistencePort associationPort;

    @Override
    public Mono<Void> associate(CapacityTechnologies association) {
        return Mono.fromCallable(() -> validate(association))
                .flatMap(valid -> associationPort.existsByCapacityId(valid.getCapacityId())
                        .flatMap(exists -> Boolean.TRUE.equals(exists)
                                ? Mono.error(new ConflictException(ExceptionMessages.TECHNOLOGIES_ALREADY_ASSOCIATED.getMessage()))
                                : technologyPort.countByIds(valid.getTechnologyIds()))
                        .flatMap(count -> count == valid.getTechnologyIds().size()
                                ? associationPort.save(valid)
                                : Mono.error(new NotFoundException(ExceptionMessages.TECHNOLOGIES_NOT_FOUND.getMessage()))));
    }

    private CapacityTechnologies validate(CapacityTechnologies association) {
        requirePositive(association.getCapacityId(), ExceptionMessages.CAPACITY_ID_INVALID);
        requireSizeBetween(association.getTechnologyIds(),
                ApplicationConstants.MIN_TECHNOLOGIES_PER_CAPACITY.getValue(),
                ApplicationConstants.MAX_TECHNOLOGIES_PER_CAPACITY.getValue(),
                ExceptionMessages.TECHNOLOGY_COUNT_OUT_OF_RANGE);
        requirePositiveIds(association.getTechnologyIds(), ExceptionMessages.TECHNOLOGY_ID_INVALID);
        requireUnique(association.getTechnologyIds(), ExceptionMessages.TECHNOLOGIES_REPEATED);
        return association;
    }
}
