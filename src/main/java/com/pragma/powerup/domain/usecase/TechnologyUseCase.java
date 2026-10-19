package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.api.ITechnologyServicePort;
import com.pragma.powerup.domain.exception.ConflictException;
import com.pragma.powerup.domain.exception.ExceptionMessages;
import com.pragma.powerup.domain.model.Technology;
import com.pragma.powerup.domain.spi.ITechnologyPersistencePort;
import com.pragma.powerup.domain.util.ApplicationConstants;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import static com.pragma.powerup.domain.util.DomainValidations.requireMaxLength;
import static com.pragma.powerup.domain.util.DomainValidations.requireText;

@RequiredArgsConstructor
public class TechnologyUseCase implements ITechnologyServicePort {
    private final ITechnologyPersistencePort persistencePort;

    @Override
    public Mono<Technology> createTechnology(Technology technology) {
        return Mono.fromCallable(() -> validate(technology))
                .flatMap(valid -> persistencePort.existsByName(valid.name())
                        .flatMap(exists -> Boolean.TRUE.equals(exists)
                                ? Mono.error(new ConflictException(ExceptionMessages.TECHNOLOGY_ALREADY_EXISTS.getMessage()))
                                : persistencePort.save(valid)));
    }

    private Technology validate(Technology technology) {
        String name = requireText(technology.name(), ExceptionMessages.TECHNOLOGY_NAME_REQUIRED);
        String description = requireText(technology.description(), ExceptionMessages.TECHNOLOGY_DESCRIPTION_REQUIRED);
        return new Technology(null,
                requireMaxLength(name, ApplicationConstants.TECHNOLOGY_NAME_MAX_LENGTH.getValue(),
                        ExceptionMessages.TECHNOLOGY_NAME_TOO_LONG),
                requireMaxLength(description, ApplicationConstants.TECHNOLOGY_DESCRIPTION_MAX_LENGTH.getValue(),
                        ExceptionMessages.TECHNOLOGY_DESCRIPTION_TOO_LONG));
    }
}
