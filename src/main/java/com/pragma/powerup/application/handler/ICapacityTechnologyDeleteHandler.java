package com.pragma.powerup.application.handler;

import java.util.List;
import reactor.core.publisher.Mono;

public interface ICapacityTechnologyDeleteHandler {
    Mono<Void> deleteForCapacities(List<Long> capacityIds);
}
