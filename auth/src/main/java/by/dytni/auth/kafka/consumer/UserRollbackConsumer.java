package by.dytni.auth.kafka.consumer;

import static by.dytni.commonevents.CommonsKafkaConstants.KAFKA_USER_ROLLBACK_TOPIC;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.stereotype.Component;

import by.dytni.auth.service.AuthenticationService;
import by.dytni.commonevents.dto.UserRollbackEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserRollbackConsumer {
    private final AuthenticationService authenticationService;

    @RetryableTopic(attempts = "5")
    @KafkaListener(topics = KAFKA_USER_ROLLBACK_TOPIC, groupId = "auth-service")
    public void consume(UserRollbackEvent event) {
        log.info("Received {}", event);
        authenticationService.rollBackUser(event);
    }
}
