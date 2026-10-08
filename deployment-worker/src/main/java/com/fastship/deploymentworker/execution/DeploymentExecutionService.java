package com.fastship.deploymentworker.execution;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeploymentExecutionService {

    private final DeploymentExecutionRepository repository;

    @Transactional
    public boolean tryClaim(UUID deploymentId) {
        return repository.tryClaim(deploymentId) == 1;
    }
}