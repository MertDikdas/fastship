package com.fastship.platformapi.repository;

import com.fastship.platformapi.domain.entity.OutboxMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OutboxMessageRepository extends JpaRepository<OutboxMessage, UUID> {
}
