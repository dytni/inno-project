package by.dytni.orderservice;

import lombok.experimental.UtilityClass;

@UtilityClass
public class OrderServiceConstants {


    public static final String DEFAULT_SHOW_MODE = "false";
    public static final String DEFAULT_PAGE = "0";
    public static final String DEFAULT_PAGE_SIZE = "10";


    public static final String ITEM_NOT_FOUND_ERROR = "Item with id: %s not found";
    public static final String ORDER_NOT_FOUND_ERROR = "Order with id: %s not found";
    public static final String BUSINESS_LOGIC_ERROR = "Error during process request";
}
