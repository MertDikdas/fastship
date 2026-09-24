package com.fastship.platformapi.exceptions;

import java.util.UUID;

public class DeploymentNotFoundException extends RuntimeException {
    public DeploymentNotFoundException(UUID deploymentId) {
        super("Deployment with id " + deploymentId + " not found");
    }
}
