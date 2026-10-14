package com.pragma.powerup.domain.api;

import java.util.List;
import reactor.core.publisher.Mono;

public interface ICapacityTechnologyDeleteServicePort {
    Mono<Void> deleteForCapacities(List<Long> capacityIds);
}
