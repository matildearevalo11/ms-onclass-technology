package com.pragma.powerup;

import com.pragma.powerup.domain.exception.ConflictException;
import com.pragma.powerup.domain.model.Technology;
import com.pragma.powerup.domain.spi.ITechnologyPersistencePort;
import com.pragma.powerup.infrastructure.out.r2dbc.repository.ITechnologyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@org.springframework.context.annotation.Import(JwtTestConfiguration.class)
@SpringBootTest
@Tag("postgres")
@ActiveProfiles("postgres-test")
class PostgresTechnologyIntegrationTest {

    @Autowired
    private ITechnologyPersistencePort persistencePort;

    @Autowired
    private ITechnologyRepository repository;

    @BeforeEach
    void clearTechnologies() {
        StepVerifier.create(repository.deleteAll()).verifyComplete();
    }

    @Test
    void uniqueIndexRejectsConcurrentWritesAndAdapterTranslatesTheError() {
        Flux<Boolean> attempts = Flux.just("Concurrent", "concurrent")
                .flatMap(name -> persistencePort.save(new Technology(null, name, "Description"))
                        .map(saved -> true)
                        .onErrorReturn(ConflictException.class, false));
        StepVerifier.create(attempts.collectList())
                .expectNextMatches(results -> results.size() == 2
                        && results.stream().filter(Boolean.TRUE::equals).count() == 1)
                .verifyComplete();
        StepVerifier.create(repository.count()).expectNext(1L).verifyComplete();
    }
}
