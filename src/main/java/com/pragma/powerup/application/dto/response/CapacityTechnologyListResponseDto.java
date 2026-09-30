package com.pragma.powerup.application.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CapacityTechnologyListResponseDto {
    private final Long capacityId;
    private final List<TechnologySummaryDto> technologies;
}
