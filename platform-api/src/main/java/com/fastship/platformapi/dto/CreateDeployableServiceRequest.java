package com.fastship.platformapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CreateDeployableServiceRequest(
        @NotNull
        UUID projectId,
        @NotBlank
        @Size(min = 2, max = 120)
        String name,
        @NotBlank
        @Size(min = 2, max = 255)
        String configPath
) {
}
