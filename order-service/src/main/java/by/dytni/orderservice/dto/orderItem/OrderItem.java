package by.dytni.orderservice.dto.orderItem;

import by.dytni.orderservice.dto.item.Item;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {
    private Long id;
    private Item item;
    private Integer quantity;
}
