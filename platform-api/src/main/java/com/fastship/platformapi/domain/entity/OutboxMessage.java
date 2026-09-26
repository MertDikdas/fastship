package com.fastship.platformapi.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "outbox_messages")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OutboxMessage {

    @Id
    private UUID id;

    @Column(name = "aggregate_id", nullable = false)
    private UUID aggregateId;

    @Column(nullable = false, length = 255)
    private String topic;

    @Column(name = "message_key", nullable = false, length = 255)
    private String messageKey;

    @Column(name = "message_type", nullable = false, length = 100)
    private String messageType;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String payload;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "published_at")
    private Instant publishedAt;

    @Column(name = "attempt_count", nullable = false)
    private int attemptCount;

    @Column(name = "last_error", length = 2000)
    private String lastError;

    public OutboxMessage(
            UUID aggregateId,
            String topic,
            String messageKey,
            String messageType,
            String payload
    ) {
        this.id = UUID.randomUUID();
        this.aggregateId = aggregateId;
        this.topic = topic;
        this.messageKey = messageKey;
        this.messageType = messageType;
        this.payload = payload;
        this.createdAt = Instant.now();
        this.attemptCount = 0;
    }

    public void registerAttempt(){
        this.attemptCount++;
    }

    public void markPublished(){
        this.publishedAt = Instant.now();
    }

    public void markFailed(String error){
        if(error == null){
            this.lastError = "Unknown Kafka publishing error";
            return;
        }

        this.lastError = error.length()>2000 ? error.substring(0, 2000) : error;
    }


}
