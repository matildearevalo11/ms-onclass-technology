package com.pragma.powerup.infrastructure.out.r2dbc.repository;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.relational.core.mapping.Column;

@Getter
@AllArgsConstructor
public class CapacityTechnologyView {
    @Column("capacity_id")
    private final Long capacityId;
    @Column("technology_id")
    private final Long technologyId;
    @Column("technology_name")
    private final String technologyName;
}
