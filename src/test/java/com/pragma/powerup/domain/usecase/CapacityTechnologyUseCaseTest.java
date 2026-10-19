package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.exception.ConflictException;
import com.pragma.powerup.domain.exception.NotFoundException;
import com.pragma.powerup.domain.model.CapacityTechnologies;
import com.pragma.powerup.domain.spi.ICapacityTechnologyPersistencePort;
import com.pragma.powerup.domain.spi.ITechnologyPersistencePort;
import java.util.List;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import static org.mockito.Mockito.*;

class CapacityTechnologyUseCaseTest {
    @Test
    void associatesOnlyExistingTechnologies() {
        var technologies = mock(ITechnologyPersistencePort.class);
        var associations = mock(ICapacityTechnologyPersistencePort.class);
        var request = new CapacityTechnologies(1L, List.of(1L, 2L, 3L));
        when(associations.existsByCapacityId(1L)).thenReturn(Mono.just(false));
        when(technologies.countByIds(request.getTechnologyIds())).thenReturn(Mono.just(3L));
        when(associations.save(request)).thenReturn(Mono.empty());
        StepVerifier.create(new CapacityTechnologyUseCase(technologies, associations).associate(request)).verifyComplete();
        verify(associations).save(request);
    }

    @Test
    void rejectsMissingTechnologiesWithoutSaving() {
        var technologies = mock(ITechnologyPersistencePort.class);
        var associations = mock(ICapacityTechnologyPersistencePort.class);
        var request = new CapacityTechnologies(1L, List.of(1L, 2L, 3L));
        when(associations.existsByCapacityId(1L)).thenReturn(Mono.just(false));
        when(technologies.countByIds(request.getTechnologyIds())).thenReturn(Mono.just(2L));
        StepVerifier.create(new CapacityTechnologyUseCase(technologies, associations).associate(request))
                .expectError(NotFoundException.class).verify();
        verify(associations, never()).save(any());
    }

    @Test
    void rejectsAnAlreadyAssociatedCapacity() {
        var technologies = mock(ITechnologyPersistencePort.class);
        var associations = mock(ICapacityTechnologyPersistencePort.class);
        when(associations.existsByCapacityId(1L)).thenReturn(Mono.just(true));
        StepVerifier.create(new CapacityTechnologyUseCase(technologies, associations)
                .associate(new CapacityTechnologies(1L, List.of(1L, 2L, 3L))))
                .expectError(ConflictException.class).verify();
        verifyNoInteractions(technologies);
        verify(associations, never()).save(any());
    }

    @Test
    void acceptsTheTechnologyCountLimits() {
        for (int count : new int[] {3, 20}) {
            var technologies = mock(ITechnologyPersistencePort.class);
            var associations = mock(ICapacityTechnologyPersistencePort.class);
            var request = new CapacityTechnologies(1L, java.util.stream.LongStream.rangeClosed(1, count).boxed().toList());
            when(associations.existsByCapacityId(1L)).thenReturn(Mono.just(false));
            when(technologies.countByIds(request.getTechnologyIds())).thenReturn(Mono.just((long) count));
            when(associations.save(request)).thenReturn(Mono.empty());

            StepVerifier.create(new CapacityTechnologyUseCase(technologies, associations).associate(request))
                    .verifyComplete();
        }
    }

    @Test
    void rejectsInvalidAssociationsBeforeQueryingPersistence() {
        var invalid = List.of(
                new CapacityTechnologies(1L, List.of(1L, 2L)),
                new CapacityTechnologies(1L, java.util.stream.LongStream.rangeClosed(1, 21).boxed().toList()),
                new CapacityTechnologies(1L, List.of(1L, 1L, 2L)),
                new CapacityTechnologies(1L, List.of(0L, 2L, 3L)),
                new CapacityTechnologies(1L, java.util.Arrays.asList(1L, null, 3L)),
                new CapacityTechnologies(1L, null),
                new CapacityTechnologies(null, List.of(1L, 2L, 3L)));
        var technologies = mock(ITechnologyPersistencePort.class);
        var associations = mock(ICapacityTechnologyPersistencePort.class);

        invalid.forEach(request -> StepVerifier.create(
                        new CapacityTechnologyUseCase(technologies, associations).associate(request))
                .expectError(com.pragma.powerup.domain.exception.ValidationException.class).verify());
        verifyNoInteractions(technologies, associations);
    }
}
