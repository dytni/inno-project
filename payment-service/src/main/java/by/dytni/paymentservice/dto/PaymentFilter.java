package by.dytni.paymentservice.dto;

import org.springframework.data.domain.Pageable;

import by.dytni.paymentservice.repository.entity.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentFilter {

    private Long userId;
    private Long orderId;
    private PaymentStatus status;
    @Builder.Default
    private Integer page = 0;
    @Builder.Default
    private Integer size = 10;

}
