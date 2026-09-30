package com.pragma.powerup.infrastructure.out.r2dbc.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@AllArgsConstructor
@Table("capacity_technologies")
public class CapacityTechnologyEntity {
    @Id
    private final Long id;
    private final Long capacityId;
    private final Long technologyId;
}
