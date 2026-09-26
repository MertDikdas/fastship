package com.fastship.platformapi.messaging.command;

import java.time.Instant;
import java.util.UUID;

public record DeploymentRequestedCommand(
        UUID deploymentId,
        UUID serviceId,
        Instant requestedAt,
        int schemaVersion
) {
}
