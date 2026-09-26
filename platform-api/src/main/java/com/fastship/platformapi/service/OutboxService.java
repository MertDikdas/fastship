package com.fastship.platformapi.service;

import com.fastship.platformapi.domain.entity.Deployment;
import com.fastship.platformapi.domain.entity.OutboxMessage;
import com.fastship.platformapi.messaging.KafkaTopics;
import com.fastship.platformapi.messaging.command.DeploymentRequestedCommand;
import com.fastship.platformapi.repository.OutboxMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.json.JsonMapper;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class OutboxService {

    private final OutboxMessageRepository outboxMessageRepository;
    private final JsonMapper jsonMapper;

    @Transactional(propagation = Propagation.MANDATORY)
    public void saveDeploymentRequested(Deployment deployment){
        Instant now = Instant.now();
        DeploymentRequestedCommand command =
                new DeploymentRequestedCommand(
                        deployment.getId(),
                        deployment.getDeployableService().getId(),
                        now,
                        1
                );
        String payload = jsonMapper.writeValueAsString(command);
        OutboxMessage outboxMessage = new OutboxMessage(
                deployment.getId(),
                KafkaTopics.DEPLOYMENT_COMMANDS,
                deployment.getId().toString(),
                "DeploymentRequested",
                payload
        );
        outboxMessageRepository.save(outboxMessage);

    }
}
