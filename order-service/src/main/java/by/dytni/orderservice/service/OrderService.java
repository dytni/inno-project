package by.dytni.orderservice.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import by.dytni.orderservice.dto.order.Order;
import by.dytni.orderservice.dto.order.OrderFilter;
import by.dytni.orderservice.dto.order.OrderMaker;
import by.dytni.orderservice.dto.order.OrderUpdater;

public interface OrderService {
    Order createOrder(OrderMaker orderMaker);

    Order updateOrder(OrderUpdater orderUpdater, Long orderId);

    Order deleteOrder(Long orderId);

    Page<Order> getAllOrders(OrderFilter filter);

    Page<Order> getOrdersByUserId(Long userId, Pageable pageable);

    Order getOrderById(Long orderId);

    boolean existsByIdAndUserId(Long orderId, Long userId);
}
