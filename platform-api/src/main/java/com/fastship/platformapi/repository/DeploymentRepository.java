package com.fastship.platformapi.repository;

import com.fastship.platformapi.domain.entity.Deployment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DeploymentRepository extends JpaRepository<Deployment, UUID> {

}
