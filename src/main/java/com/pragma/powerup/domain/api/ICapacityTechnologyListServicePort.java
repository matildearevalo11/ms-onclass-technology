package com.pragma.powerup.domain.api;

import com.pragma.powerup.domain.model.CapacityTechnologyList;
import java.util.List;
import reactor.core.publisher.Flux;

public interface ICapacityTechnologyListServicePort {
    Flux<CapacityTechnologyList> listByCapacityIds(List<Long> capacityIds);
}
