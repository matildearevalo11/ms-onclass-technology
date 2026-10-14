package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.spi.ICapacityTechnologyPersistencePort;
import com.pragma.powerup.domain.spi.ITechnologyPersistencePort;
import java.util.List;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import static org.mockito.Mockito.*;

class CapacityTechnologyDeleteUseCaseTest {
    @Test
    void deletesOnlyTechnologiesWithoutRemainingCapacityReferences() {
        var associations = mock(ICapacityTechnologyPersistencePort.class);
        var technologies = mock(ITechnologyPersistencePort.class);
        when(associations.findTechnologyIdsByCapacityIds(List.of(1L)))
                .thenReturn(Flux.just(10L, 20L, 20L));
        when(associations.deleteByCapacityIds(List.of(1L))).thenReturn(Mono.empty());
        when(associations.findReferencedTechnologyIds(List.of(10L, 20L)))
                .thenReturn(Flux.just(20L));
        when(technologies.deleteByIds(List.of(10L))).thenReturn(Mono.empty());

        StepVerifier.create(new CapacityTechnologyDeleteUseCase(associations, technologies)
                .deleteForCapacities(List.of(1L))).verifyComplete();
        var order = inOrder(associations, technologies);
        order.verify(associations).deleteByCapacityIds(List.of(1L));
        order.verify(associations).findReferencedTechnologyIds(List.of(10L, 20L));
        order.verify(technologies).deleteByIds(List.of(10L));
    }
}
