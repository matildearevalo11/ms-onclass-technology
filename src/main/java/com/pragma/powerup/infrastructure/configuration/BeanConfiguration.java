package com.pragma.powerup.infrastructure.configuration;

import com.pragma.powerup.domain.api.ITechnologyServicePort;
import com.pragma.powerup.domain.api.ICapacityTechnologyServicePort;
import com.pragma.powerup.domain.spi.ICapacityTechnologyPersistencePort;
import com.pragma.powerup.domain.usecase.CapacityTechnologyUseCase;
import com.pragma.powerup.domain.spi.ITechnologyPersistencePort;
import com.pragma.powerup.domain.usecase.TechnologyUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {
    @Bean
    ICapacityTechnologyServicePort capacityTechnologyServicePort(ITechnologyPersistencePort technologyPort,
            ICapacityTechnologyPersistencePort associationPort) {
        return new CapacityTechnologyUseCase(technologyPort, associationPort);
    }

    @Bean
    ITechnologyServicePort technologyServicePort(ITechnologyPersistencePort persistencePort) {
        return new TechnologyUseCase(persistencePort);
    }
}
