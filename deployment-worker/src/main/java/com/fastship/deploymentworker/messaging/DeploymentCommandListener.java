package com.fastship.deploymentworker.messaging;

import com.fastship.deploymentworker.execution.DeploymentExecutionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.json.JsonMapper;

@Component
@RequiredArgsConstructor
@Slf4j
public class DeploymentCommandListener {

    private final JsonMapper jsonMapper;
    private final DeploymentExecutionService executionService;

    @KafkaListener(
            topics = "fastship.deployment.commands.v1"
    )
    public void handle(ConsumerRecord<String, String> record)
            throws Exception {
        DeploymentRequestedCommand command =
                jsonMapper.readValue(
                        record.value(),
                        DeploymentRequestedCommand.class
                );

        if(command.schemaVersion()!=1){
            throw new Exception(
                    "Unsupported schema version: " + command.schemaVersion()
            );
        }
        boolean claimed =
                executionService.tryClaim(command.deploymentId());

        if (!claimed) {
            log.info(
                    "Skipping already claimed deployment deploymentId={} partition={} offset={}",
                    command.deploymentId(),
                    record.partition(),
                    record.offset()
            );
            return;
        }

        log.info(
                "Claimed DeploymentRequested deploymentId={} serviceId={} partition={} offset={} key={}",
                command.deploymentId(),
                command.serviceId(),
                record.partition(),
                record.offset(),
                record.key()
        );

    }
}
