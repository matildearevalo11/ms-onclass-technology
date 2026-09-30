package com.pragma.powerup.application.handler.impl;

import com.pragma.powerup.application.dto.request.TechnologyRequestDto;
import com.pragma.powerup.application.dto.response.TechnologyResponseDto;
import com.pragma.powerup.application.handler.ITechnologyHandler;
import com.pragma.powerup.application.mapper.TechnologyMapper;
import com.pragma.powerup.domain.api.ITechnologyServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class TechnologyHandler implements ITechnologyHandler {

    private final ITechnologyServicePort servicePort;
    private final TechnologyMapper mapper;

    @Override
    public Mono<TechnologyResponseDto> createTechnology(TechnologyRequestDto request) {
        return servicePort.createTechnology(mapper.toModel(request)).map(mapper::toResponse);
    }
}
