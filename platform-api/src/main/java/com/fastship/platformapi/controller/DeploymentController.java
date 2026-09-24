package com.fastship.platformapi.controller;

import com.fastship.platformapi.domain.entity.Deployment;
import com.fastship.platformapi.dto.CreateDeploymentRequest;
import com.fastship.platformapi.dto.DeploymentResponse;
import com.fastship.platformapi.service.DeploymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/deployments")
@RequiredArgsConstructor
public class DeploymentController {

    private final DeploymentService deploymentService;

    @PostMapping
    public ResponseEntity<DeploymentResponse> createDeployment(
            @Valid
            @RequestBody CreateDeploymentRequest request
    ) {
        Deployment deployment =
                deploymentService.createDeployment(request.serviceId());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(DeploymentResponse.from(deployment));
    }

    @GetMapping
    public ResponseEntity<List<DeploymentResponse>> getAllDeployments(){
        List<DeploymentResponse> responses = deploymentService
                .getAllDeployments()
                .stream()
                .map(item -> DeploymentResponse.from(item))
                .toList();

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{deploymentId}")
    public ResponseEntity<DeploymentResponse> getDeploymentById(
            @PathVariable UUID deploymentId
    ){
        Deployment deployment = deploymentService.getDeploymentById(deploymentId);
        return ResponseEntity.ok(
                DeploymentResponse.from(deployment)
        );
    }
}
