package com.fastship.deploymentworker.execution;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

@Component
@RequiredArgsConstructor
@Slf4j
public class DeploymentRecoveryScanner {

    private static final Duration STALE_AFTER =
            Duration.ofSeconds(30);

    private static final int BATCH_SIZE = 20;

    private final DeploymentExecutionRepository repository;
    private final DeploymentExecutionService executionService;

    @Scheduled(
            fixedDelayString = "${fastship.worker.recovery-scan-interval-ms:15000}"
    )
    public void recoverStaleExecutions() {

        Instant staleBefore =
                Instant.now().minus(STALE_AFTER);

        var staleExecutions =
                repository.findStaleExecutions(
                        staleBefore,
                        BATCH_SIZE
                );

        for (var execution : staleExecutions) {

            boolean reclaimed =
                    executionService.tryReclaim(
                            execution.getDeploymentId()
                    );

            if (reclaimed) {
                log.info(
                        "Reclaimed stale deployment deploymentId={} serviceId={}",
                        execution.getDeploymentId(),
                        execution.getServiceId()
                );
            }
        }
    }
}