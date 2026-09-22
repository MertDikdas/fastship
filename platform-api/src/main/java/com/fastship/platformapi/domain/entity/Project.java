package com.fastship.platformapi.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name="projects"
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Project {

    @Id
    private UUID id = UUID.randomUUID();

    @Column(
            name = "name",
            nullable = false,
            unique = true,
            length = 120
    )
    private String name;

    @Column(
            name = "repository_url",
            nullable = false,
            length = 1000
    )
    private String repositoryUrl;

    @Column(
            name="created_at",
            nullable = false
    )
    private Instant createdAt = Instant.now();

    public Project(String name, String repositoryUrl) {
        this.id = UUID.randomUUID();
        this.name = requireText(name, "name");
        this.repositoryUrl = requireText(repositoryUrl, "repositoryUrl");
        this.createdAt = Instant.now();
    }

    private static String requireText(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " cannot be blank");
        }
        return value;
    }

}
