package com.pragma.powerup.infrastructure.out.r2dbc.adapter;

import com.pragma.powerup.domain.model.CapacityTechnologyRow;
import com.pragma.powerup.domain.model.TechnologySummary;
import com.pragma.powerup.domain.spi.ICapacityTechnologyQueryPort;
import com.pragma.powerup.infrastructure.out.r2dbc.repository.ICapacityTechnologyRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
@RequiredArgsConstructor
public class CapacityTechnologyQueryR2dbcAdapter implements ICapacityTechnologyQueryPort {
    private final ICapacityTechnologyRepository repository;

    @Override
    public Flux<CapacityTechnologyRow> findByCapacityIds(List<Long> capacityIds) {
        return repository.findTechnologiesByCapacityIds(capacityIds)
                .map(row -> new CapacityTechnologyRow(row.getCapacityId(),
                        new TechnologySummary(row.getTechnologyId(), row.getTechnologyName())));
    }
}
