package by.dytni.auth.kafka;

import static by.dytni.auth.AuthConstant.KAFKA_USER_LOGIN_TOPIC;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import by.dytni.auth.service.AuthenticationService;
import by.dytni.commonevents.dto.UserChangedLoginEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserLoginChangedConsumer {

    private final AuthenticationService authenticationService;

    @KafkaListener(topics = KAFKA_USER_LOGIN_TOPIC, groupId = "auth-service")
    public void consume(UserChangedLoginEvent event) {
        log.info("Received {}", event);
        authenticationService.changeLogin(event.oldLogin(), event.newLogin());
    }
}
