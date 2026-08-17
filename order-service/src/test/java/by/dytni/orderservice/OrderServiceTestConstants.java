package by.dytni.orderservice;

import java.math.BigDecimal;

import by.dytni.orderservice.repository.entity.OrderStatus;
import lombok.experimental.UtilityClass;

@UtilityClass
public class OrderServiceTestConstants {



    public static final Long USER_ID = 1L;
    public static final Long ORDER_ID = 1L;
    public static final BigDecimal ITEM_PRICE = BigDecimal.TEN;
    public static final BigDecimal ORDER_PRICE = BigDecimal.valueOf(20.00);
    public static final OrderStatus ORDER_STATUS = OrderStatus.CREATED;
    public static final Integer ITEM_QUANTITY= 2;
}
