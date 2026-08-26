package by.dytni.userservice.kafka.consumer;


import static by.dytni.commonevents.CommonsKafkaConstants.KAFKA_USER_CREATED_TOPIC;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import by.dytni.commonevents.dto.UserCreatedEvent;
import by.dytni.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserCreatedConsumer {

    private final UserService userService;

    @KafkaListener(topics = KAFKA_USER_CREATED_TOPIC, groupId = "user-service")
    public void consume(UserCreatedEvent event) {
        log.info("Received {}", event);
        userService.createUserFromAuth(event);
    }
}
