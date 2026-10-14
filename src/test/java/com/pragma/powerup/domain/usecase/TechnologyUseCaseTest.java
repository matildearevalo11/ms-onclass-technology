package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.exception.ConflictException;
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

}
