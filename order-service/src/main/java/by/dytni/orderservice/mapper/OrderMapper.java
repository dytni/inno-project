package by.dytni.orderservice.mapper;


import java.math.BigDecimal;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.ReportingPolicy;

import by.dytni.orderservice.dto.order.Order;
import by.dytni.orderservice.dto.order.OrderMaker;
import by.dytni.orderservice.dto.order.OrderUpdater;
import by.dytni.orderservice.repository.entity.OrderEntity;
import by.dytni.orderservice.repository.entity.OrderItemEntity;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring",
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT,
        uses = {OrderItemMapper.class})
public abstract class OrderMapper {

    @Mapping(target = "id" , ignore = true)
    public abstract OrderEntity dtoToEntity(OrderMaker orderMaker);

    public abstract OrderEntity updateEntity(@MappingTarget OrderEntity orderEntity, OrderUpdater orderUpdater);

    public abstract Order entityToDto(OrderEntity orderEntity);

    @AfterMapping
    protected void linkOrderItems(@MappingTarget OrderEntity orderEntity) {
        if (orderEntity.getOrderItems() != null) {
            BigDecimal total = new BigDecimal(0);
            for (OrderItemEntity item : orderEntity.getOrderItems()) {
                item.setOrder(orderEntity);
                total = total.add(item.getItem().getPrice().multiply(new BigDecimal(item.getQuantity())));
            }
            orderEntity.setTotalPrice(total);
        }
    }

}
