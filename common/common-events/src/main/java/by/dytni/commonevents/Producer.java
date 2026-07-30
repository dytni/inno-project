package by.dytni.commonevents;


import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class Producer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void send(Object message, String topic) {
        var record = new ProducerRecord<String, Object>(topic, message);

        kafkaTemplate.send(record)
                .thenAccept(result -> {
                    log.debug("successfully sent message = {}", message);
                })
                .exceptionally(ex -> {
                    log.error("failed to send message = {}", message);
                    return null;
                });
    }
}