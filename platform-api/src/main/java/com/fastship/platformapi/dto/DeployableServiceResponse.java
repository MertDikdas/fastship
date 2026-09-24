package com.fastship.platformapi.dto;

import com.fastship.platformapi.domain.entity.DeployableService;

import java.time.Instant;
import java.util.UUID;

public record DeployableServiceResponse(
        UUID id,
        UUID projectId,
        String name,
        String configPath,
        Instant createdAt
) {

    public static DeployableServiceResponse from(DeployableService deployableService) {
        return new DeployableServiceResponse(
                deployableService.getId(),
                deployableService.getProject().getId(),
                deployableService.getName(),
                deployableService.getConfigPath(),
                deployableService.getCreatedAt()
        );
    }
}
