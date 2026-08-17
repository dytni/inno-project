package by.dytni.orderservice.repository.criteria;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;

import by.dytni.orderservice.repository.entity.OrderStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderCriteria {
    LocalDateTime from;
    LocalDateTime to;
    List<OrderStatus> statuses;
    Boolean showDeleted;
    Pageable pageable;
}
