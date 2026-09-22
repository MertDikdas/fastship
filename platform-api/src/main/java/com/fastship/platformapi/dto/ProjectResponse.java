package com.fastship.platformapi.dto;

import com.fastship.platformapi.domain.entity.Project;

import java.time.Instant;
import java.util.UUID;

public record ProjectResponse(
        UUID id,
        String name,
        String repositoryUrl,
        Instant createdAt
) {
    public static ProjectResponse from(Project project){
        return new ProjectResponse(
                project.getId(),
                project.getName(),
                project.getRepositoryUrl(),
                project.getCreatedAt()
        );
    }
}
