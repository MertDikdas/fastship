package com.fastship.platformapi.repository;

import com.fastship.platformapi.domain.entity.OutboxMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface OutboxMessageRepository extends JpaRepository<OutboxMessage, UUID> {

    @Query(
            value = """
                SELECT *
                FROM outbox_messages
                WHERE published_at IS NULL
                ORDER BY created_at ASC
                LIMIT :limit
                FOR UPDATE SKIP LOCKED
                """,
            nativeQuery = true
    )
    List<OutboxMessage> findUnpublishedBatch(
            @Param("limit") int limit
    );
}
