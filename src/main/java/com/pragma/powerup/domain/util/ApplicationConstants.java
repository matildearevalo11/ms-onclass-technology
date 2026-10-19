package com.pragma.powerup.domain.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApplicationConstants {
    TECHNOLOGY_NAME_MAX_LENGTH(50),
    TECHNOLOGY_DESCRIPTION_MAX_LENGTH(90),
    MIN_TECHNOLOGIES_PER_CAPACITY(3),
    MAX_TECHNOLOGIES_PER_CAPACITY(20);

    private final int value;
}
