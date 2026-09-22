package com.fastship.platformapi.domain.entity;

public enum DeploymentStatus {
    QUEUED,
    PREPARING,
    BUILDING,
    STARTING,
    HEALTH_CHECKING,
    SUCCESS,
    FAILED
}