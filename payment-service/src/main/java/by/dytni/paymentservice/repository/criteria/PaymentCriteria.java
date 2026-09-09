package by.dytni.paymentservice.repository.criteria;

import org.springframework.data.domain.Pageable;

import by.dytni.paymentservice.repository.entity.PaymentStatus;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class PaymentCriteria {
    private Long userId;
    private Long orderId;
    private PaymentStatus status;
    private Pageable pageable;
}
