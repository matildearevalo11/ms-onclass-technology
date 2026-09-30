package com.pragma.powerup.application.handler.impl;

import com.pragma.powerup.application.dto.request.CapacityTechnologyRequestDto;
import com.pragma.powerup.application.handler.ICapacityTechnologyHandler;
import com.pragma.powerup.application.mapper.CapacityTechnologyMapper;
import com.pragma.powerup.domain.api.ICapacityTechnologyServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CapacityTechnologyHandler implements ICapacityTechnologyHandler {
    private final ICapacityTechnologyServicePort servicePort;
    private final CapacityTechnologyMapper mapper;

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Mono<Void> associate(CapacityTechnologyRequestDto request) {
        return servicePort.associate(mapper.toModel(request));
    }
}
