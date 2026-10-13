package com.fastship.deploymentworker.execution;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "deployment_executions", schema = "worker")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeploymentExecution {

    @Id
    @Column(name = "deployment_id", nullable = false)
    private UUID deploymentId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private DeploymentExecutionStatus status;

    @Column(name = "worker_id", nullable = false, length = 100)
    private String workerId;

    @Column(name = "heartbeat_at", nullable = false)
    private Instant heartbeatAt;

    @Column(name = "attempt_count", nullable = false)
    private int attemptCount;

    @Column(name = "service_id", nullable = false)
    private UUID serviceId;

    @Column(name = "started_at", nullable = false)
    private Instant startedAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Column(name = "completed_at")
    private Instant completedAt;

    public DeploymentExecution(
            UUID deploymentId,
            UUID serviceId,
            String workerId
    ) {
        Instant now = Instant.now();

        this.deploymentId = deploymentId;
        this.serviceId = serviceId;
        this.status = DeploymentExecutionStatus.PROCESSING;
        this.workerId = workerId;
        this.startedAt = now;
        this.updatedAt = now;
        this.heartbeatAt = now;
        this.attemptCount = 1;
    }

    public void markSucceeded() {
        this.status = DeploymentExecutionStatus.SUCCEEDED;
        this.updatedAt = Instant.now();
        this.completedAt = this.updatedAt;
    }

    public void markFailed() {
        this.status = DeploymentExecutionStatus.FAILED;
        this.updatedAt = Instant.now();
        this.completedAt = this.updatedAt;
    }
}