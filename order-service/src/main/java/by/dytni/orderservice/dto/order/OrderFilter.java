package by.dytni.orderservice.dto.order;

import java.time.LocalDateTime;
import java.util.List;

import by.dytni.orderservice.repository.entity.OrderStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderFilter {

    LocalDateTime from;

    LocalDateTime to;

    List<OrderStatus> statuses;


    @Builder.Default
    Boolean showDeleted = Boolean.FALSE;

    @Builder.Default
    private Integer page = 0;
    @Builder.Default
    private Integer size = 10;
}
