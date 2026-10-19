package com.pragma.powerup.infrastructure.out.r2dbc.repository;

import com.pragma.powerup.infrastructure.out.r2dbc.entity.CapacityTechnologyEntity;
import java.util.List;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ICapacityTechnologyRepository extends ReactiveCrudRepository<CapacityTechnologyEntity, Long> {
    Mono<Boolean> existsByCapacityId(Long capacityId);

    @Query("SELECT ct.capacity_id, t.id AS technology_id, t.name AS technology_name "
            + "FROM capacity_technologies ct JOIN technologies t ON t.id = ct.technology_id "
            + "WHERE ct.capacity_id IN (:capacityIds) ORDER BY ct.capacity_id, t.id")
    Flux<CapacityTechnologyView> findTechnologiesByCapacityIds(List<Long> capacityIds);

    @Query("SELECT DISTINCT technology_id FROM capacity_technologies WHERE capacity_id IN (:capacityIds)")
    Flux<Long> findTechnologyIdsByCapacityIds(List<Long> capacityIds);

    @Query("DELETE FROM capacity_technologies WHERE capacity_id IN (:capacityIds)")
    @Modifying
    Mono<Void> deleteByCapacityIds(List<Long> capacityIds);
}
