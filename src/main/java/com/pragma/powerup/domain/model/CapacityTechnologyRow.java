package com.pragma.powerup.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CapacityTechnologyRow {
    private final Long capacityId;
    private final TechnologySummary technology;
}
