package com.pragma.powerup.application.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CapacityTechnologyListRequestDto {
    @NotEmpty(message = "Capacity IDs are required.")
    private List<@NotNull(message = "Capacity ID is required.")
            @Positive(message = "Capacity ID must be positive.") Long> capacityIds;
}
