package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.api.ICapacityTechnologyDeleteServicePort;
import com.pragma.powerup.domain.spi.ICapacityTechnologyPersistencePort;
import com.pragma.powerup.domain.spi.ITechnologyPersistencePort;
import java.util.List;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class CapacityTechnologyDeleteUseCase implements ICapacityTechnologyDeleteServicePort {
    private final ICapacityTechnologyPersistencePort associations;
    private final ITechnologyPersistencePort technologies;

    @Override
    public Mono<Void> deleteForCapacities(List<Long> capacityIds) {
        return associations.findTechnologyIdsByCapacityIds(capacityIds).distinct().collectList()
                .flatMap(ids -> associations.deleteByCapacityIds(capacityIds)
                        .then(ids.isEmpty() ? Mono.empty()
                                : associations.findReferencedTechnologyIds(ids).collectList()
                                        .flatMap(referenced -> {
                                            List<Long> orphanIds = ids.stream()
                                                    .filter(id -> !referenced.contains(id)).toList();
                                            return orphanIds.isEmpty() ? Mono.empty() : technologies.deleteByIds(orphanIds);
                                        })));
    }
}
