package com.pragma.powerup.application.handler.impl;

import com.pragma.powerup.application.handler.ICapacityTechnologyDeleteHandler;
import com.pragma.powerup.domain.api.ICapacityTechnologyDeleteServicePort;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CapacityTechnologyDeleteHandler implements ICapacityTechnologyDeleteHandler {
    private final ICapacityTechnologyDeleteServicePort servicePort;

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Mono<Void> deleteForCapacities(List<Long> capacityIds) {
        return servicePort.deleteForCapacities(capacityIds);
    }
}
