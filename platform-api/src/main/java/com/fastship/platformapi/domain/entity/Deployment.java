package com.fastship.platformapi.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(
        name= "deployments"
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Deployment {
    @Id
    private UUID id= UUID.randomUUID();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name= "service_id", nullable = false)
    private DeployableService deployableService;

    @Column(name= "status", nullable = false, length = 100)
    @Enumerated(EnumType.STRING)
    private DeploymentStatus deploymentStatus;

    @Column(name= "failure_stage", length = 100)
    @Enumerated(EnumType.STRING)
    private DeploymentStage failureStage;

    @Column(name = "failure_code", length = 100)
    private String failureCode;

    @Column(name = "failure_message", length = 4000)
    private String failureMessage;

    @Column(name= "created_at", nullable = false)
    private Instant createdAt = Instant.now();

    @Column(name = "started_at")
    private Instant startedAt;

    @Column(name="finished_at")
    private Instant finishedAt;

    public Deployment(DeployableService deployableService) {
        this.id = UUID.randomUUID();
        this.deployableService =
                Objects.requireNonNull(deployableService, "deployableService cannot be null");

        this.deploymentStatus = DeploymentStatus.QUEUED;
        this.createdAt = Instant.now();
    }

    public void markPreparing() {
        transition(
                DeploymentStatus.QUEUED,
                DeploymentStatus.PREPARING
        );

        this.startedAt = Instant.now();
    }

    public void markBuilding() {
        transition(
                DeploymentStatus.PREPARING,
                DeploymentStatus.BUILDING
        );
    }

    public void markStarting() {
        transition(
                DeploymentStatus.BUILDING,
                DeploymentStatus.STARTING
        );
    }

    public void markHealthChecking() {
        transition(
                DeploymentStatus.STARTING,
                DeploymentStatus.HEALTH_CHECKING
        );
    }

    public void markSuccess() {
        transition(
                DeploymentStatus.HEALTH_CHECKING,
                DeploymentStatus.SUCCESS
        );

        this.finishedAt = Instant.now();
    }

    public void markFailed(String failureCode, String failureMessage) {

        this.failureStage = switch (deploymentStatus) {
            case PREPARING -> DeploymentStage.PREPARING;
            case BUILDING -> DeploymentStage.BUILDING;
            case STARTING -> DeploymentStage.STARTING;
            case HEALTH_CHECKING -> DeploymentStage.HEALTH_CHECKING;

            default -> throw new IllegalStateException(
                    "Deployment cannot fail from status " + deploymentStatus
            );
        };

        this.failureCode = requireText(failureCode, "failureCode");
        this.failureMessage = requireText(failureMessage, "failureMessage");
        this.deploymentStatus = DeploymentStatus.FAILED;
        this.finishedAt = Instant.now();
    }
    private void transition(
            DeploymentStatus expected,
            DeploymentStatus next
    ) {
        if (deploymentStatus != expected) {
            throw new IllegalStateException(
                    "Cannot transition deployment from "
                            + deploymentStatus
                            + " to "
                            + next
            );
        }

        deploymentStatus = next;
    }
    private static String requireText(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " cannot be blank");
        }
        return value;
    }
}
