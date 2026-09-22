package com.fastship.platformapi.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;


@Entity
@Table(
        name= "deployable_services"
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeployableService {

    @Id
    private UUID id = UUID.randomUUID();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Column(name= "name", nullable = false, length = 120)
    private String name;

    @Column(name= "config_path", nullable = false, length = 255)
    private String configPath;

    @Column(name="created_at", nullable = false)
    private Instant createdAt = Instant.now();

    public DeployableService(
            Project project,
            String name,
            String configPath
    ) {
        this.id = UUID.randomUUID();
        this.project = Objects.requireNonNull(project, "project cannot be null");
        this.name = requireText(name, "name");
        this.configPath = requireText(configPath, "configPath");
        this.createdAt = Instant.now();
    }

    private static String requireText(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " cannot be blank");
        }
        return value;
    }
}
