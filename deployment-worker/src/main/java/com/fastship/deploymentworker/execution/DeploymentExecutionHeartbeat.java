package com.fastship.deploymentworker.execution;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class DeploymentExecutionHeartbeat {

    private final DeploymentExecutionRepository repository;
    private final WorkerIdentity workerIdentity;

    @Scheduled(
            fixedDelayString = "${fastship.worker.heartbeat-interval-ms:10000}"
    )
    @Transactional
    public void refresh() {

        int updated = repository.refreshHeartbeats(
                workerIdentity.getId()
        );

        if (updated > 0) {
            log.debug(
                    "Refreshed heartbeat for {} deployment execution(s)",
                    updated
            );
        }
    }
}