package com.fastship.deploymentworker.execution;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class WorkerIdentity {

    private final String id = UUID.randomUUID().toString();

    public String getId() {
        return id;
    }
}