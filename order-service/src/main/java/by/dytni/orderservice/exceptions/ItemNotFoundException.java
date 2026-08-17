package by.dytni.orderservice.exceptions;

import static by.dytni.orderservice.OrderServiceConstants.ITEM_NOT_FOUND_ERROR;

public class ItemNotFoundException extends RuntimeException {
    public ItemNotFoundException(Long id) {
        super(String.format(ITEM_NOT_FOUND_ERROR, id));
    }
}
