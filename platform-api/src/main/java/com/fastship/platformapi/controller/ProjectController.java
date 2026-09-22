package com.fastship.platformapi.controller;

import com.fastship.platformapi.domain.entity.Project;
import com.fastship.platformapi.dto.CreateProjectRequest;
import com.fastship.platformapi.dto.ProjectResponse;
import com.fastship.platformapi.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;

    @PostMapping
    public ResponseEntity<ProjectResponse> createProject(
            @Valid
            @RequestBody CreateProjectRequest request
    ){
        Project project = projectService.createProject(request.name(),request.repositoryUrl());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ProjectResponse.from(project));
    }

}
