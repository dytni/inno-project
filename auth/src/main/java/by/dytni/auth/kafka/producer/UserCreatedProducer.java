package by.dytni.auth.kafka.producer;


import static by.dytni.commonevents.CommonsKafkaConstants.KAFKA_USER_CREATED_TOPIC;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import by.dytni.commonevents.Producer;
import by.dytni.commonevents.dto.UserCreatedEvent;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserCreatedProducer {
    private final Producer producer;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional
    public void send(UserCreatedEvent event) {
        producer.send(event, KAFKA_USER_CREATED_TOPIC);
    }
}
