package com.fastship.deploymentworker.execution;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface DeploymentExecutionRepository
        extends JpaRepository<DeploymentExecution, UUID> {
    @Modifying
    @Query(value = """
        INSERT INTO worker.deployment_executions (
            deployment_id,
            status,
            started_at,
            updated_at
        )
        VALUES (
            :deploymentId,
            'PROCESSING',
            CURRENT_TIMESTAMP,
            CURRENT_TIMESTAMP
        )
        ON CONFLICT (deployment_id) DO NOTHING
        """, nativeQuery = true)
    int tryClaim(@Param("deploymentId") UUID deploymentId);

}
