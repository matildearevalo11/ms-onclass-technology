package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.exception.ConflictException;
import com.pragma.powerup.domain.exception.ExceptionMessages;
import com.pragma.powerup.domain.exception.ValidationException;
import com.pragma.powerup.domain.model.Technology;
import com.pragma.powerup.domain.spi.ITechnologyPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TechnologyUseCaseTest {

    @Mock
    private ITechnologyPersistencePort persistencePort;
    private TechnologyUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new TechnologyUseCase(persistencePort);
    }

    @Test
    void acceptsExactLimitsIncludingUnicodeCharacters() {
        Technology input = new Technology(null, "🚀".repeat(50), "a".repeat(90));
        when(persistencePort.existsByName(input.name())).thenReturn(Mono.just(false));
        when(persistencePort.save(input)).thenReturn(Mono.just(new Technology(1L, input.name(), input.description())));
        StepVerifier.create(useCase.createTechnology(input))
                .expectNext(new Technology(1L, input.name(), input.description())).verifyComplete();
    }

    @Test
    void rejectsDuplicateWithoutSaving() {
        when(persistencePort.existsByName("Java")).thenReturn(Mono.just(true));
        StepVerifier.create(useCase.createTechnology(new Technology(null, "Java", "Language")))
                .expectError(ConflictException.class).verify();
        verify(persistencePort, never()).save(any());
    }


    @Test
    void stripsNameAndDescriptionBeforeSaving() {
        var normalized = new Technology(null, "Java", "Language");
        when(persistencePort.existsByName("Java")).thenReturn(Mono.just(false));
        when(persistencePort.save(normalized)).thenReturn(Mono.just(new Technology(1L, "Java", "Language")));

        StepVerifier.create(useCase.createTechnology(new Technology(null, "  Java ", " Language ")))
                .expectNext(new Technology(1L, "Java", "Language")).verifyComplete();
    }

    @Test
    void rejectsInvalidTechnologiesWithoutTouchingPersistence() {
        var invalid = java.util.Map.of(
                new Technology(null, " ", "Language"), ExceptionMessages.TECHNOLOGY_NAME_REQUIRED,
                new Technology(null, "Java", null), ExceptionMessages.TECHNOLOGY_DESCRIPTION_REQUIRED,
                new Technology(null, "a".repeat(51), "Language"), ExceptionMessages.TECHNOLOGY_NAME_TOO_LONG,
                new Technology(null, "Java", "a".repeat(91)), ExceptionMessages.TECHNOLOGY_DESCRIPTION_TOO_LONG);

        invalid.forEach((technology, message) -> StepVerifier.create(useCase.createTechnology(technology))
                .expectErrorSatisfies(error -> org.assertj.core.api.Assertions.assertThat(error)
                        .isInstanceOf(ValidationException.class).hasMessage(message.getMessage()))
                .verify());
        verifyNoInteractions(persistencePort);
    }
}
