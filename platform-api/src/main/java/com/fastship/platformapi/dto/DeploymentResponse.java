package com.fastship.platformapi.dto;


import com.fastship.platformapi.domain.entity.Deployment;
import com.fastship.platformapi.domain.entity.DeploymentStatus;

import java.time.Instant;
import java.util.UUID;

public record DeploymentResponse (
        UUID id,
        UUID deployableServiceId,
        DeploymentStatus deploymentStatus,
        Instant createdAt
){
    public static DeploymentResponse from(Deployment deployment) {
        return new DeploymentResponse(
                deployment.getId(),
                deployment.getDeployableService().getId(),
                deployment.getDeploymentStatus(),
                deployment.getCreatedAt()
        );
    }
}
