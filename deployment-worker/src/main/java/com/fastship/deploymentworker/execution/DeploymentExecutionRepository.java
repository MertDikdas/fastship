package com.fastship.deploymentworker.execution;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.UUID;

public interface DeploymentExecutionRepository
        extends JpaRepository<DeploymentExecution, UUID> {

    @Modifying
    @Query(value = """
    INSERT INTO worker.deployment_executions (
        deployment_id,
        status,
        worker_id,
        started_at,
        updated_at,
        heartbeat_at,
        attempt_count
    )
    VALUES (
        :deploymentId,
        'PROCESSING',
        :workerId,
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP,
        1
    )
    ON CONFLICT (deployment_id) DO NOTHING
    """, nativeQuery = true)
    int tryClaim(
            @Param("deploymentId") UUID deploymentId,
            @Param("workerId") String workerId
    );

    @Modifying
    @Query(value = """
    UPDATE worker.deployment_executions
    SET worker_id = :workerId,
        heartbeat_at = CURRENT_TIMESTAMP,
        updated_at = CURRENT_TIMESTAMP,
        attempt_count = attempt_count + 1
    WHERE deployment_id = :deploymentId
      AND status = 'PROCESSING'
      AND heartbeat_at < :staleBefore
    """, nativeQuery = true)
    int tryReclaimStale(
            @Param("deploymentId") UUID deploymentId,
            @Param("workerId") String workerId,
            @Param("staleBefore") Instant staleBefore
    );

    @Modifying
    @Query(value = """
    UPDATE worker.deployment_executions
    SET heartbeat_at = CURRENT_TIMESTAMP,
        updated_at = CURRENT_TIMESTAMP
    WHERE worker_id = :workerId
      AND status = 'PROCESSING'
    """, nativeQuery = true)
    int refreshHeartbeats(@Param("workerId") String workerId);
}
