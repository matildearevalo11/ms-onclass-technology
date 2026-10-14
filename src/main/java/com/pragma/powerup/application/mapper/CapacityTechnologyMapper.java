package com.pragma.powerup.application.mapper;

import com.pragma.powerup.application.dto.request.CapacityTechnologyRequestDto;
import com.pragma.powerup.domain.model.CapacityTechnologies;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class CapacityTechnologyMapper {
    public CapacityTechnologies toModel(CapacityTechnologyRequestDto request) {
        return new CapacityTechnologies(request.getCapacityId(), List.copyOf(request.getTechnologyIds()));
    }
}
