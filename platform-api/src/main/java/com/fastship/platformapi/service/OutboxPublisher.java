package com.fastship.platformapi.service;

import com.fastship.platformapi.domain.entity.OutboxMessage;
import com.fastship.platformapi.repository.OutboxMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class OutboxPublisher {

    private static final int BATCH_SIZE = 50;

    private final OutboxMessageRepository outboxMessageRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Scheduled(
            fixedDelayString = "${fastship.outbox.publish-delay-ms:1000}"
    )
    @Transactional
    public void publishPendigMessages(){
        List<OutboxMessage> messages = outboxMessageRepository.findUnpublishedBatch(BATCH_SIZE);

        for(OutboxMessage message : messages){
            publish(message);
        }
    }

    private void publish(OutboxMessage message){
        message.registerAttempt();

        try{
            kafkaTemplate
                    .send(
                            message.getTopic(),
                            message.getMessageKey(),
                            message.getPayload()
                    )
                    .get(5, TimeUnit.SECONDS);

            message.markPublished();

            log.info(
                    "Published outbox message id={} type={} aggregateId={}",
                    message.getId(),
                    message.getMessageType(),
                    message.getAggregateId()
            );
        }catch (Exception exception){
            message.markFailed(exception.getMessage());

            log.error(
                    "Failed to publish outbox message id={} type={} aggregateId={}",
                    message.getId(),
                    message.getMessageType(),
                    message.getAggregateId(),
                    exception
            );
        }
    }
}
