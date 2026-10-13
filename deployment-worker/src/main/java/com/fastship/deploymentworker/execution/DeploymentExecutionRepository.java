package com.fastship.deploymentworker.execution;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface DeploymentExecutionRepository
        extends JpaRepository<DeploymentExecution, UUID> {

    @Modifying
    @Query(value = """
    INSERT INTO worker.deployment_executions (
        deployment_id,
        service_id,
        status,
        worker_id,
        started_at,
        updated_at,
        heartbeat_at,
        attempt_count
    )
    VALUES (
        :deploymentId,
        :serviceId,
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
            @Param("serviceId") UUID serviceId,
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

    @Query(value = """
    SELECT
        deployment_id AS deploymentId,
        service_id AS serviceId
    FROM worker.deployment_executions
    WHERE status = 'PROCESSING'
      AND heartbeat_at < :staleBefore
    ORDER BY heartbeat_at ASC
    LIMIT :limit
    """, nativeQuery = true)
    List<StaleDeploymentExecution> findStaleExecutions(
            @Param("staleBefore") Instant staleBefore,
            @Param("limit") int limit
    );
}
