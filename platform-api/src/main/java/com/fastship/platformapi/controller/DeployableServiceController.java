package com.fastship.platformapi.controller;

import com.fastship.platformapi.domain.entity.DeployableService;
import com.fastship.platformapi.dto.CreateDeployableServiceRequest;
import com.fastship.platformapi.dto.DeployableServiceResponse;
import com.fastship.platformapi.service.DeployableServiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/services")
@RequiredArgsConstructor
public class DeployableServiceController {
    private final DeployableServiceService deployableServiceService;

    @PostMapping
    public ResponseEntity<DeployableServiceResponse> getDeployableService(
            @Valid @RequestBody CreateDeployableServiceRequest request
    ) {
        DeployableService deployableService =
                deployableServiceService.createDeployableService(
                        request.projectId(),
                        request.name(),
                        request.configPath()
                );
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(DeployableServiceResponse.from(deployableService));
    }

}
