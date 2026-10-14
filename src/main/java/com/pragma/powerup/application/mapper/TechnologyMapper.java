package com.pragma.powerup.application.mapper;

import com.pragma.powerup.application.dto.request.TechnologyRequestDto;
import com.pragma.powerup.application.dto.response.TechnologyResponseDto;
import com.pragma.powerup.domain.model.Technology;
import org.springframework.stereotype.Component;

@Component
public class TechnologyMapper {

    public Technology toModel(TechnologyRequestDto request) {
        return new Technology(null, request.getName(), request.getDescription());
    }

    public TechnologyResponseDto toResponse(Technology technology) {
        return new TechnologyResponseDto(technology.id(), technology.name(), technology.description());
    }
}
