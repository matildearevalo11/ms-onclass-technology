package com.pragma.powerup.application.handler;

import com.pragma.powerup.application.dto.request.CapacityTechnologyListRequestDto;
import com.pragma.powerup.application.dto.response.CapacityTechnologyListResponseDto;
import reactor.core.publisher.Flux;

public interface ICapacityTechnologyListHandler {
    Flux<CapacityTechnologyListResponseDto> listByCapacityIds(CapacityTechnologyListRequestDto request);
}
