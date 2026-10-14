package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.api.ICapacityTechnologyListServicePort;
import com.pragma.powerup.domain.model.CapacityTechnologyList;
import com.pragma.powerup.domain.model.CapacityTechnologyRow;
import com.pragma.powerup.domain.spi.ICapacityTechnologyQueryPort;
import java.util.List;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
public class CapacityTechnologyListUseCase implements ICapacityTechnologyListServicePort {
    private final ICapacityTechnologyQueryPort queryPort;

    @Override
    public Flux<CapacityTechnologyList> listByCapacityIds(List<Long> capacityIds) {
        List<Long> distinctIds = capacityIds.stream().distinct().toList();
        if (distinctIds.isEmpty()) {
            return Flux.empty();
        }
        return queryPort.findByCapacityIds(distinctIds).collectMultimap(CapacityTechnologyRow::getCapacityId)
                .flatMapMany(rows -> Flux.fromIterable(distinctIds)
                        .map(id -> new CapacityTechnologyList(id, rows.getOrDefault(id, List.of()).stream()
                                .map(CapacityTechnologyRow::getTechnology).toList())));
    }
}
