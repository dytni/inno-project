package by.dytni.paymentservice.kafka;

import static by.dytni.commonevents.CommonsKafkaConstants.KAFKA_PAYMENT_CREATED_TOPIC;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import by.dytni.commonevents.Producer;
import by.dytni.commonevents.dto.PaymentCreateEvent;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PaymentCreateProducer {
    private final Producer producer;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void send(PaymentCreateEvent event) {
        producer.send(event, KAFKA_PAYMENT_CREATED_TOPIC);
    }
}

