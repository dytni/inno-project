package by.dytni.paymentservice.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import by.dytni.paymentservice.repository.entity.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Payment {
    private String id;

    private Long orderId;

    private Long userId;

    private PaymentStatus status;

    private LocalDateTime timestamp;

    private BigDecimal paymentAmount;
}
