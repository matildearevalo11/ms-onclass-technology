package com.pragma.powerup;

import com.pragma.powerup.domain.exception.ExceptionMessages;
import com.pragma.powerup.application.handler.ITechnologyHandler;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verifyNoInteractions;
import com.pragma.powerup.infrastructure.out.r2dbc.repository.ITechnologyRepository;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.springframework.core.io.ClassPathResource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.test.StepVerifier;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ActiveProfiles("test")
@Import(JwtTestConfiguration.class)
class OnClassApplicationTest {
    @Autowired
    private ITechnologyRepository repository;
    @Autowired
    private JwtEncoder encoder;
    @MockitoSpyBean
    private ITechnologyHandler handler;
    @Autowired
    private WebTestClient client;
    @MockitoSpyBean
    private com.pragma.powerup.infrastructure.out.r2dbc.repository.ICapacityTechnologyRepository associationRepository;

    @BeforeEach
    void setUp() {
        StepVerifier.create(associationRepository.deleteAll().then(repository.deleteAll())).verifyComplete();
    }

    @Test
    void servesStaticOpenApiThroughSwaggerUi() throws IOException {
        String contract = new ClassPathResource("openapi.yaml")
                .getContentAsString(StandardCharsets.UTF_8);
        client.get().uri("/openapi.yaml").exchange().expectStatus().isOk()
                .expectBody(String.class).isEqualTo(contract);
        client.get().uri("/swagger-ui.html").exchange().expectStatus().is3xxRedirection();
        client.get().uri("/webjars/swagger-ui/index.html").exchange().expectStatus().isOk();
        client.get().uri("/v3/api-docs/swagger-config").exchange().expectStatus().isOk()
                .expectBody().jsonPath("$.url").isEqualTo("/api/v1/openapi.yaml");
    }

    @Test
    void registersTechnologyAndRejectsDuplicateWithTheDocumentedError() {
        var request = Map.of("name", " Java ", "description", " Language ");
        post(token("ADMIN"), request).expectStatus().isCreated().expectBody()
                .jsonPath("$.id").isNumber().jsonPath("$.name").isEqualTo("Java")
                .jsonPath("$.description").isEqualTo("Language");
        post(token("ADMIN"), Map.of("name", "java", "description", "Language"))
                .expectStatus().isEqualTo(409).expectBody().jsonPath("$.errors.message")
                .isEqualTo(ExceptionMessages.TECHNOLOGY_ALREADY_EXISTS.getMessage());
        StepVerifier.create(repository.count()).expectNext(1L).verifyComplete();
        String boundaryName = "a".repeat(50);
        String boundaryDescription = "a".repeat(90);
        post(token("ADMIN"), Map.of("name", boundaryName,
                "description", boundaryDescription)).expectStatus().isCreated().expectBody()
                .jsonPath("$.name").isEqualTo(boundaryName)
                .jsonPath("$.description").isEqualTo(boundaryDescription);
    }

    @Test
    void rejectsNonAdminWithTheDocumentedError() {
        post(token("PERSON"), Map.of()).expectStatus().isForbidden().expectBody()
                .jsonPath("$.errors.message").isEqualTo(ExceptionMessages.ACCESS_DENIED.getMessage());
    }

    @Test
    void rejectsInvalidSignatureWithTheDocumentedError() throws java.text.ParseException, com.nimbusds.jose.JOSEException {
        String valid = token("ADMIN");
        var parsed = com.nimbusds.jwt.SignedJWT.parse(valid);
        var altered = new com.nimbusds.jwt.SignedJWT(parsed.getHeader(), parsed.getJWTClaimsSet());
        altered.sign(new com.nimbusds.jose.crypto.MACSigner("different-test-secret-not-for-deployment-1234567890"));
        post(altered.serialize(), Map.of()).expectStatus().isUnauthorized().expectBody()
                .jsonPath("$.errors.message").isEqualTo(ExceptionMessages.AUTHENTICATION_REQUIRED.getMessage());
    }

    @Test
    void rejectsInvalidRequestsBeforeCallingTheHandler() {
        var invalidRequests = Map.of(
                Map.of("name", "Java"), ExceptionMessages.TECHNOLOGY_DESCRIPTION_REQUIRED,
                Map.of("name", " ", "description", "Language"), ExceptionMessages.TECHNOLOGY_NAME_REQUIRED,
                Map.of("name", "a".repeat(51), "description", "Language"), ExceptionMessages.TECHNOLOGY_NAME_TOO_LONG,
                Map.of("name", "Java", "description", "a".repeat(91)), ExceptionMessages.TECHNOLOGY_DESCRIPTION_TOO_LONG);
        String adminToken = token("ADMIN");
        invalidRequests.forEach((request, message) ->
                post(adminToken, request).expectStatus().isBadRequest().expectBody()
                        .jsonPath("$.errors.message").isEqualTo(message.getMessage()));
        verifyNoInteractions(handler);
    }

    @Test
    void associatesExistingTechnologiesAtBothLimitsAndRejectsRepeatedRegistration() {
        var technologies = java.util.stream.IntStream.rangeClosed(1, 20)
                .mapToObj(i -> new com.pragma.powerup.infrastructure.out.r2dbc.entity.TechnologyEntity(null, "Tech" + i, "Description")).toList();
        var ids = repository.saveAll(technologies).map(com.pragma.powerup.infrastructure.out.r2dbc.entity.TechnologyEntity::id)
                .collectList().block();
        associate(1L, ids.subList(0, 3)).expectStatus().isNoContent();
        associate(2L, ids).expectStatus().isNoContent();
        associate(1L, ids.subList(0, 3)).expectStatus().isEqualTo(409);
        StepVerifier.create(associationRepository.count()).expectNext(23L).verifyComplete();
    }

    @Test
    void rejectsUnknownAndDuplicateTechnologiesWithoutAssociations() {
        associate(1L, List.of(1L, 2L, 3L)).expectStatus().isNotFound();
        associate(1L, List.of(1L, 1L, 2L)).expectStatus().isBadRequest();
        StepVerifier.create(associationRepository.count()).expectNext(0L).verifyComplete();
    }

    @Test
    void rollsBackAllAssociationsWhenAnInsertFails() {
        var ids = repository.saveAll(java.util.stream.IntStream.rangeClosed(1, 3)
                .mapToObj(i -> new com.pragma.powerup.infrastructure.out.r2dbc.entity.TechnologyEntity(null, "Rollback" + i, "Description"))
                .toList()).map(com.pragma.powerup.infrastructure.out.r2dbc.entity.TechnologyEntity::id).collectList().block();
        doReturn(reactor.core.publisher.Mono.error(new org.springframework.dao.DataIntegrityViolationException("Insert failed")))
                .when(associationRepository).save(org.mockito.ArgumentMatchers.argThat(entity -> entity.getTechnologyId().equals(ids.get(2))));
        associate(1L, ids).expectStatus().isEqualTo(503);
        StepVerifier.create(associationRepository.count()).expectNext(0L).verifyComplete();
    }

    @Test
    void deletesExclusiveTechnologiesAndKeepsSharedOnes() {
        var ids = repository.saveAll(List.of(
                new com.pragma.powerup.infrastructure.out.r2dbc.entity.TechnologyEntity(null, "Java", "Language"),
                new com.pragma.powerup.infrastructure.out.r2dbc.entity.TechnologyEntity(null, "SQL", "Database"),
                new com.pragma.powerup.infrastructure.out.r2dbc.entity.TechnologyEntity(null, "Docker", "Containers"),
                new com.pragma.powerup.infrastructure.out.r2dbc.entity.TechnologyEntity(null, "Kotlin", "Language")))
                .map(com.pragma.powerup.infrastructure.out.r2dbc.entity.TechnologyEntity::id).collectList().block();
        associate(10L, ids.subList(0, 3)).expectStatus().isNoContent();
        associate(20L, ids.subList(1, 4)).expectStatus().isNoContent();

        client.delete().uri("/technologies/capacities?capacityIds=10")
                .headers(headers -> headers.setBearerAuth(token("ADMIN")))
                .exchange().expectStatus().isNoContent();
        StepVerifier.create(repository.count()).expectNext(3L).verifyComplete();
        StepVerifier.create(associationRepository.count()).expectNext(3L).verifyComplete();
        client.get().uri("/technologies/capacities?capacityIds=20")
                .headers(headers -> headers.setBearerAuth(token("ADMIN"))).exchange()
                .expectStatus().isOk().expectBody().jsonPath("$[0].technologies.length()").isEqualTo(3);

        client.delete().uri("/technologies/capacities?capacityIds=20")
                .headers(headers -> headers.setBearerAuth(token("ADMIN")))
                .exchange().expectStatus().isNoContent();
        StepVerifier.create(repository.count()).expectNext(0L).verifyComplete();
    }

    @Test
    void looksUpOnlyTechnologyIdsAndNamesForMultipleCapacities() {
        var ids = repository.saveAll(List.of(
                new com.pragma.powerup.infrastructure.out.r2dbc.entity.TechnologyEntity(null, "Java", "Language"),
                new com.pragma.powerup.infrastructure.out.r2dbc.entity.TechnologyEntity(null, "SQL", "Database"),
                new com.pragma.powerup.infrastructure.out.r2dbc.entity.TechnologyEntity(null, "Docker", "Containers")))
                .map(com.pragma.powerup.infrastructure.out.r2dbc.entity.TechnologyEntity::id).collectList().block();
        associate(10L, ids).expectStatus().isNoContent();
        associate(20L, ids).expectStatus().isNoContent();

        client.get().uri("/technologies/capacities?capacityIds=10,20,30")
                .headers(headers -> headers.setBearerAuth(token("ADMIN")))
                .exchange().expectStatus().isOk().expectBody()
                .jsonPath("$[0].capacityId").isEqualTo(10)
                .jsonPath("$[0].technologies.length()").isEqualTo(3)
                .jsonPath("$[0].technologies[0].name").isEqualTo("Java")
                .jsonPath("$[0].technologies[0].description").doesNotExist()
                .jsonPath("$[1].capacityId").isEqualTo(20)
                .jsonPath("$[2].capacityId").isEqualTo(30)
                .jsonPath("$[2].technologies.length()").isEqualTo(0);

        client.get().uri("/technologies/capacities?capacityIds=-1")
                .headers(headers -> headers.setBearerAuth(token("ADMIN")))
                .exchange().expectStatus().isBadRequest();
        client.get().uri("/technologies/capacities?capacityIds=10")
                .headers(headers -> headers.setBearerAuth(token("PERSON")))
                .exchange().expectStatus().isForbidden();
    }

    private WebTestClient.ResponseSpec associate(Long capacityId, List<Long> ids) {
        return client.post().uri("/technologies/capacities").headers(h -> h.setBearerAuth(token("ADMIN")))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Map.of("capacityId", capacityId, "technologyIds", ids)).exchange();
    }

    private WebTestClient.ResponseSpec post(String token, Object body) {
        return client.post().uri("/technologies").headers(headers -> headers.setBearerAuth(token))
                .contentType(MediaType.APPLICATION_JSON).bodyValue(body).exchange();
    }

    private String token(String role) {
        Instant now = Instant.now();
        var claims = JwtClaimsSet.builder().issuer("http://localhost:8081").subject("1")
                .audience(List.of("on-class-technology")).issuedAt(now).expiresAt(now.plusSeconds(300))
                .claim("role", role).build();
        return encoder.encode(JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS256).build(), claims))
                .getTokenValue();
    }
}
