package com.pragma.powerup.domain.spi;

import com.pragma.powerup.domain.model.CapacityTechnologyRow;
import java.util.List;
import reactor.core.publisher.Flux;

public interface ICapacityTechnologyQueryPort {
    Flux<CapacityTechnologyRow> findByCapacityIds(List<Long> capacityIds);
}
