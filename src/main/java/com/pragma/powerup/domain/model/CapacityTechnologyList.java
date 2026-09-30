package com.pragma.powerup.domain.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CapacityTechnologyList {
    private final Long capacityId;
    private final List<TechnologySummary> technologies;
}
