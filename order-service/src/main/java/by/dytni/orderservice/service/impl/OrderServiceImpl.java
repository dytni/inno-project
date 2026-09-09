package by.dytni.orderservice.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import by.dytni.orderservice.dto.order.Order;
import by.dytni.orderservice.dto.order.OrderFilter;
import by.dytni.orderservice.dto.order.OrderMaker;
import by.dytni.orderservice.dto.order.OrderUpdater;
import by.dytni.orderservice.exceptions.OrderNotFoundException;
import by.dytni.orderservice.mapper.OrderCriteriaMapper;
import by.dytni.orderservice.mapper.OrderMapper;
import by.dytni.orderservice.repository.OrderRepository;
import by.dytni.orderservice.repository.criteria.OrderCriteria;
import by.dytni.orderservice.repository.entity.OrderEntity;
import by.dytni.orderservice.repository.entity.OrderStatus;
import by.dytni.orderservice.repository.specifications.OrderSpecification;
import by.dytni.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {


    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderCriteriaMapper criteriaMapper;

    @Override
    @Transactional
    public Order createOrder(OrderMaker orderMaker) {
        OrderEntity orderEntity = orderMapper.dtoToEntity(orderMaker);
        return orderMapper.entityToDto(orderRepository.save(orderEntity));
    }

    @Override
    @Transactional
    public Order updateOrder(OrderUpdater orderUpdater, Long orderId) {
        OrderEntity orderEntity = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
        orderRepository.save(orderMapper.updateEntity(orderEntity, orderUpdater));
        return orderMapper.entityToDto(orderEntity);
    }

    @Override
    @Transactional
    public Order deleteOrder(Long orderId) {
        OrderEntity orderEntity = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
        orderEntity.setDeleted(true);
        return orderMapper.entityToDto(orderRepository.save(orderEntity));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Order> getAllOrders(OrderFilter filter) {
        OrderCriteria criteria = criteriaMapper.dtoToCriteria(filter);
        Specification<OrderEntity> specification = OrderSpecification.getSpecification(criteria);
        return orderRepository.findAll(specification, criteria.getPageable()).map(orderMapper::entityToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Order> getOrdersByUserId(Long userId, Pageable pageable) {
        return orderRepository.getOrdersByUserId(userId, pageable).map(orderMapper::entityToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Order getOrderById(Long orderId) {
        return orderMapper.entityToDto(
                orderRepository.findById(orderId)
                        .orElseThrow(() -> new OrderNotFoundException(orderId))
        );
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByIdAndUserId(Long orderId, Long userId) {
        return orderRepository.existsByIdAndUserId(orderId, userId);
    }

    @Override
    @Transactional
    public void consumePayment(OrderStatus orderStatus, Long orderId) {
        OrderEntity orderEntity = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
        orderEntity.setStatus(orderStatus);
        orderRepository.save(orderEntity);
    }
}
