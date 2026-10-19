package com.pragma.powerup.infrastructure.out.r2dbc.repository;

import java.util.List;
import com.pragma.powerup.infrastructure.out.r2dbc.entity.TechnologyEntity;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface ITechnologyRepository extends ReactiveCrudRepository<TechnologyEntity, Long> {
    Mono<Long> countByIdIn(List<Long> ids);

    Mono<Boolean> existsByName(String name);

    @Modifying
    @Query("DELETE FROM technologies t WHERE t.id IN (:ids) AND NOT EXISTS ("
            + "SELECT 1 FROM capacity_technologies ct WHERE ct.technology_id = t.id)")
    Mono<Void> deleteUnreferencedByIds(List<Long> ids);
}
