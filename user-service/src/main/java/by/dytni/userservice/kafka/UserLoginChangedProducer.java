package by.dytni.userservice.kafka;

import static by.dytni.userservice.UserServiceConstants.KAFKA_USER_LOGIN_TOPIC;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import by.dytni.commonevents.Producer;
import by.dytni.commonevents.dto.UserChangedLoginEvent;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserLoginChangedProducer {
    private final Producer producer;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void send(UserChangedLoginEvent event) {
        producer.send(event, KAFKA_USER_LOGIN_TOPIC);
    }
}
