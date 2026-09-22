package com.fastship.platformapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateProjectRequest(
        @NotBlank
        @Size(min = 2, max = 100)
        String name,

        @NotBlank
        @Size(max = 1000)
        String repositoryUrl
) {
}
