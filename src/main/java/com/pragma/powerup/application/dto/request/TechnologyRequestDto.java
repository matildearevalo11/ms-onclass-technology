package com.pragma.powerup.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.Size;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TechnologyRequestDto {

    @NotBlank(message = "Technology name is required.")
    @Size(max = 50, message = "The name must not exceed 50 characters.")
    private String name;

    @NotBlank(message = "Technology description is required.")
    @Size(max = 90, message = "The description must not exceed 90 characters.")
    private String description;
}
