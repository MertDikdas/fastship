package com.fastship.platformapi.service;

import com.fastship.platformapi.domain.entity.DeployableService;
import com.fastship.platformapi.domain.entity.Project;
import com.fastship.platformapi.exceptions.ProjectNotFoundException;
import com.fastship.platformapi.repository.DeployableServiceRepository;
import com.fastship.platformapi.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeployableServiceService {

    private final ProjectRepository projectRepository;
    private final DeployableServiceRepository deployableServiceRepository;

    @Transactional
    public DeployableService createDeployableService(UUID projectId, String name, String configPath){
        Project project = projectRepository.findById(projectId).orElseThrow(()-> new ProjectNotFoundException(projectId));
        DeployableService deployableService = new DeployableService(project, name, configPath);

        return deployableServiceRepository.save(deployableService);
    }
}
