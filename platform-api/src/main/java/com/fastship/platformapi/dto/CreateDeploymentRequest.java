package com.fastship.platformapi.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateDeploymentRequest(
        @NotNull
        UUID serviceId
) {
}