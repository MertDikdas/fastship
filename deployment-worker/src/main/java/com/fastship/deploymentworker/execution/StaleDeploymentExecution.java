package com.fastship.deploymentworker.execution;

import java.util.UUID;

public interface StaleDeploymentExecution {

    UUID getDeploymentId();

    UUID getServiceId();
}