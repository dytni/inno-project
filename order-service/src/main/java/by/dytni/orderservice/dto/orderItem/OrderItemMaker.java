package by.dytni.orderservice.dto.orderItem;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemMaker {

    @NotNull
    private Long itemId;

    @NotNull
    @Positive
    private Integer quantity;
}
