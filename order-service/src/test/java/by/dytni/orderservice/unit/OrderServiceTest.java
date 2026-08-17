package by.dytni.orderservice.unit;

import static by.dytni.orderservice.OrderServiceTestConstants.ORDER_ID;
import static by.dytni.orderservice.OrderServiceTestConstants.USER_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import by.dytni.orderservice.dto.order.Order;
import by.dytni.orderservice.dto.order.OrderFilter;
import by.dytni.orderservice.dto.order.OrderMaker;
import by.dytni.orderservice.dto.order.OrderUpdater;
import by.dytni.orderservice.mapper.OrderCriteriaMapper;
import by.dytni.orderservice.mapper.OrderMapper;
import by.dytni.orderservice.repository.OrderRepository;
import by.dytni.orderservice.repository.entity.OrderEntity;
import by.dytni.orderservice.service.impl.OrderServiceImpl;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderMapper orderMapper;
    @Mock
    private OrderCriteriaMapper criteriaMapper;
    @Mock
    private Specification<OrderEntity> specification;
    @Mock
    private OrderEntity orderEntity;
    @Mock
    private Order order;
    @Mock
    private OrderMaker orderMaker;
    @Mock
    private OrderUpdater orderUpdater;
    @Mock
    private OrderFilter orderFilter;
    @Mock
    private Pageable pageable;

    @InjectMocks
    private OrderServiceImpl service;


    @AfterEach
    void complete() { verifyNoMoreInteractions(orderRepository, orderMapper, criteriaMapper, specification); }


    @Test
    void create_order() {
        when(orderMapper.dtoToEntity(orderMaker)).thenReturn(orderEntity);
        when(orderRepository.save(orderEntity)).thenReturn(orderEntity);
        when(orderMapper.entityToDto(orderEntity)).thenReturn(order);

        Order result = service.createOrder(orderMaker);

        assertThat(result).isEqualTo(order);

        verify(orderMapper, times(1)).dtoToEntity(orderMaker);
        verify(orderRepository, times(1)).save(orderEntity);
        verify(orderMapper, times(1)).entityToDto(orderEntity);
    }


    @Test
    void update_order() {

        when(orderRepository.findById(ORDER_ID)).thenReturn(Optional.of(orderEntity));
        when(orderMapper.updateEntity(orderEntity, orderUpdater)).thenReturn(orderEntity);
        when(orderRepository.save(orderEntity)).thenReturn(orderEntity);
        when(orderMapper.entityToDto(orderEntity)).thenReturn(order);


        Order result = service.updateOrder(orderUpdater, ORDER_ID);


        assertThat(result).isEqualTo(order);

        verify(orderRepository, times(1)).findById(ORDER_ID);
        verify(orderMapper, times(1)).updateEntity(orderEntity, orderUpdater);
        verify(orderRepository, times(1)).save(orderEntity);
        verify(orderMapper, times(1)).entityToDto(orderEntity);
    }

    @Test
    void delete_order() {

        when(orderRepository.findById(ORDER_ID)).thenReturn(Optional.of(orderEntity));
        when(orderRepository.save(orderEntity)).thenReturn(orderEntity);
        when(orderMapper.entityToDto(orderEntity)).thenReturn(order);

        Order result = service.deleteOrder(ORDER_ID);

        assertThat(result).isEqualTo(order);

        verify(orderRepository, times(1)).findById(ORDER_ID);
        verify(orderEntity, times(1)).setDeleted(true);
        verify(orderRepository, times(1)).save(orderEntity);
        verify(orderMapper, times(1)).entityToDto(orderEntity);
    }


    @Test
    void get_orders_by_user_id() {

        Page<OrderEntity> orderEntityPage = new PageImpl<>(List.of(orderEntity, orderEntity, orderEntity), pageable, 3);
        when(orderRepository.getOrdersByUserId(USER_ID, pageable)).thenReturn(orderEntityPage);
        when(orderMapper.entityToDto(orderEntity)).thenReturn(order);

        Page<Order> result = service.getOrderByUserId(USER_ID, pageable);

        assertThat(result).hasSize(3);
        assertThat(result.getContent().getFirst()).isEqualTo(order);

        verify(orderRepository, times(1)).getOrdersByUserId(USER_ID, pageable);
        verify(orderMapper, times(3)).entityToDto(orderEntity);
    }


    @Test
    void get_order_by_id() {
        when(orderRepository.findById(ORDER_ID)).thenReturn(Optional.of(orderEntity));
        when(orderMapper.entityToDto(orderEntity)).thenReturn(order);

        Order result = service.getOrderById(ORDER_ID);

        assertThat(result).isEqualTo(order);

        verify(orderRepository, times(1)).findById(ORDER_ID);
        verify(orderMapper, times(1)).entityToDto(orderEntity);
    }

}
