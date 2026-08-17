package by.dytni.orderservice.exceptions;

import static by.dytni.orderservice.OrderServiceConstants.ORDER_NOT_FOUND_ERROR;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(Long id) {
        super(String.format(ORDER_NOT_FOUND_ERROR, id));
    }
}
