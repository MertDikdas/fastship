package com.fastship.deploymentworker.execution;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeploymentExecutionService {

    private final DeploymentExecutionRepository repository;
    private final WorkerIdentity workerIdentity;
    private static final Duration STALE_AFTER = Duration.ofSeconds(30);

    @Transactional
    public boolean tryClaim(UUID deploymentId, UUID serviceId) {

        String workerId = workerIdentity.getId();

        if (repository.tryClaim(
                deploymentId,
                serviceId,
                workerId
        ) == 1) {
            return true;
        }

        Instant staleBefore =
                Instant.now().minus(STALE_AFTER);

        return repository.tryReclaimStale(
                deploymentId,
                workerId,
                staleBefore
        ) == 1;
    }

    @Transactional
    public boolean tryReclaim(UUID deploymentId) {

        Instant staleBefore =
                Instant.now().minus(STALE_AFTER);

        return repository.tryReclaimStale(
                deploymentId,
                workerIdentity.getId(),
                staleBefore
        ) == 1;
    }
}