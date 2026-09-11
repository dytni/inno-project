package by.dytni.userservice.kafka.producer;


import static by.dytni.commonevents.CommonsKafkaConstants.KAFKA_USER_ROLLBACK_TOPIC;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import by.dytni.commonevents.Producer;
import by.dytni.commonevents.dto.UserRollbackEvent;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserRollbackProducer {
    private final Producer producer;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
    @Transactional
    public void send(UserRollbackEvent event) {
        producer.send(event, KAFKA_USER_ROLLBACK_TOPIC);
    }
}
