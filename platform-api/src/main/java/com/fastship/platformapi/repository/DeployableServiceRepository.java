package com.fastship.platformapi.repository;

import com.fastship.platformapi.domain.entity.DeployableService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DeployableServiceRepository extends JpaRepository<DeployableService, UUID> {

}
