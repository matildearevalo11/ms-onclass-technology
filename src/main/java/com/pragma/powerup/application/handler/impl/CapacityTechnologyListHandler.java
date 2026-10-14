package com.pragma.powerup.application.handler.impl;

import com.pragma.powerup.application.dto.request.CapacityTechnologyListRequestDto;
import com.pragma.powerup.application.dto.response.CapacityTechnologyListResponseDto;
import com.pragma.powerup.application.dto.response.TechnologySummaryDto;
import com.pragma.powerup.application.handler.ICapacityTechnologyListHandler;
import com.pragma.powerup.domain.api.ICapacityTechnologyListServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
@RequiredArgsConstructor
public class CapacityTechnologyListHandler implements ICapacityTechnologyListHandler {
    private final ICapacityTechnologyListServicePort service;

    @Override
    public Flux<CapacityTechnologyListResponseDto> listByCapacityIds(CapacityTechnologyListRequestDto request) {
        return service.listByCapacityIds(request.getCapacityIds())
                .map(item -> new CapacityTechnologyListResponseDto(item.getCapacityId(),
                        item.getTechnologies().stream()
                                .map(technology -> new TechnologySummaryDto(technology.getId(), technology.getName()))
                                .toList()));
    }
}
