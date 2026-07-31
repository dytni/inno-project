package by.dytni.userservice.kafka;

import static by.dytni.userservice.UserServiceConstants.KAFKA_USER_STATUS_TOPIC;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import by.dytni.commonevents.Producer;
import by.dytni.commonevents.dto.UserStatusChangedEvent;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserStatusChangedProducer {
    private final Producer producer;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void send(UserStatusChangedEvent event) {
        producer.send(event, KAFKA_USER_STATUS_TOPIC);
    }
}
