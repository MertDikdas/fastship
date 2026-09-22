package com.fastship.platformapi.service;

import com.fastship.platformapi.domain.entity.Project;
import com.fastship.platformapi.dto.CreateProjectRequest;
import com.fastship.platformapi.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;

    @Transactional
    public Project createProject(String name, String repositoryUrl){

        Project project = new Project(name, repositoryUrl);
        return projectRepository.save(project);

    }
}
