package com.pragma.powerup.infrastructure.out.r2dbc.mapper;

import com.pragma.powerup.domain.model.Technology;
import com.pragma.powerup.infrastructure.out.r2dbc.entity.TechnologyEntity;
import org.springframework.stereotype.Component;

@Component
public class TechnologyEntityMapper {

    public TechnologyEntity toEntity(Technology technology) {
        return new TechnologyEntity(technology.id(), technology.name(), technology.description());
    }

    public Technology toModel(TechnologyEntity entity) {
        return new Technology(entity.id(), entity.name(), entity.description());
    }
}
