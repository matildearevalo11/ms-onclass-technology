package com.pragma.powerup.infrastructure.out.r2dbc.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("technologies")
public record TechnologyEntity(@Id Long id, String name, String description) {
}
