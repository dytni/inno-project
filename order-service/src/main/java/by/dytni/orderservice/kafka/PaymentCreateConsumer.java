package by.dytni.orderservice.kafka;

import static by.dytni.commonevents.CommonsKafkaConstants.KAFKA_PAYMENT_CREATED_TOPIC;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import by.dytni.commonevents.dto.PaymentCreateEvent;
import by.dytni.orderservice.repository.entity.OrderStatus;
import by.dytni.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentCreateConsumer {

    private final OrderService orderService;

    @KafkaListener(topics = KAFKA_PAYMENT_CREATED_TOPIC, groupId = "order-service")
    @Transactional
    public void consume(PaymentCreateEvent event) {
        log.info("Received {}", event);
        OrderStatus status = event.paidStatus() ? OrderStatus.PAID : OrderStatus.REJECTED;
        orderService.consumePayment(status, event.orderId());
    }
}
