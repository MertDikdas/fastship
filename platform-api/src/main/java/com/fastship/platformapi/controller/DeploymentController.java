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
}
