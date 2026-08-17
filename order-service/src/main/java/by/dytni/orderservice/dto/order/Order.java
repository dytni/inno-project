package by.dytni.orderservice.dto.order;

import java.math.BigDecimal;
import java.util.List;

import by.dytni.orderservice.dto.orderItem.OrderItem;
import by.dytni.orderservice.repository.entity.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private Long id;
    private Long userId;
    private OrderStatus status;
    private BigDecimal totalPrice;
    private List<OrderItem> orderItems;
}
