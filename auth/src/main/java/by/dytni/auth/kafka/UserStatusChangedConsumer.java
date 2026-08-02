package by.dytni.auth.kafka;

import static by.dytni.auth.AuthConstant.KAFKA_USER_STATUS_TOPIC;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import by.dytni.auth.service.AuthenticationService;
import by.dytni.commonevents.dto.UserStatusChangedEvent;
import by.dytni.commonsecurity.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserStatusChangedConsumer {


    private final UserStatusService userStatusService;

    private final AuthenticationService authenticationService;

    @KafkaListener(topics = KAFKA_USER_STATUS_TOPIC, groupId = "auth-service")
    public void consume(UserStatusChangedEvent event) {
        log.info("Received {}", event);
        if (event.active()) {
            userStatusService.activate(event.userId());
        } else {
            userStatusService.deactivate(event.userId());
        }
        authenticationService.changeStatus(event.email(), event.active());
    }

}
