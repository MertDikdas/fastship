package com.fastship.platformapi.service;

import com.fastship.platformapi.domain.entity.DeployableService;
import com.fastship.platformapi.domain.entity.Deployment;
import com.fastship.platformapi.domain.entity.DeploymentStatus;
import com.fastship.platformapi.exceptions.DeployableServiceNotFoundException;
import com.fastship.platformapi.exceptions.DeploymentNotFoundException;
import com.fastship.platformapi.repository.DeployableServiceRepository;
import com.fastship.platformapi.repository.DeploymentRepository;
import com.fastship.platformapi.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeploymentService {

    private final DeploymentRepository deploymentRepository;
    private final DeployableServiceRepository deployableServiceRepository;

    @Transactional
    public Deployment createDeployment(UUID serviceId) {
        DeployableService deployableService = deployableServiceRepository
                .findById(serviceId)
                .orElseThrow(() -> new DeployableServiceNotFoundException(serviceId));

        Deployment deployment = new Deployment(deployableService);

        return deploymentRepository.save(deployment);
    }

    @Transactional(readOnly = true)
    public List<Deployment> getAllDeployments(){
        return deploymentRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Deployment getDeploymentById(UUID deploymentId){
        return deploymentRepository
                .findById(deploymentId)
                .orElseThrow(()-> new DeploymentNotFoundException(deploymentId));
    }
}
