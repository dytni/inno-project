package by.dytni.orderservice.dto.order;

import java.util.List;

import by.dytni.orderservice.dto.orderItem.OrderItemUpdater;
import by.dytni.orderservice.repository.entity.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderUpdater {
    @NotNull
    private OrderStatus status;
    @NotNull
    private List<OrderItemUpdater> orderItems;
}
