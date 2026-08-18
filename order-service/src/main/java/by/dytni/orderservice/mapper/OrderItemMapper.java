package by.dytni.orderservice.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;

import by.dytni.orderservice.dto.orderItem.OrderItem;
import by.dytni.orderservice.dto.orderItem.OrderItemMaker;
import by.dytni.orderservice.dto.orderItem.OrderItemUpdater;
import by.dytni.orderservice.repository.ItemRepository;
import by.dytni.orderservice.repository.entity.ItemEntity;
import by.dytni.orderservice.repository.entity.OrderItemEntity;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring",
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT,
        uses = {ItemMapper.class})
public abstract class OrderItemMapper {


    @Autowired
    private ItemRepository itemRepository;

    @Mapping(target = "item", ignore = true)
    public abstract OrderItemEntity dtoToEntity(OrderItemMaker orderItemMaker);

    public abstract OrderItem entityToDto(OrderItemEntity orderItemEntity);

    @Mapping(target = "item", ignore = true)
    public abstract OrderItemEntity updateEntity(OrderItemUpdater orderItemUpdater);


    public void updateListEntity(
            @MappingTarget List<OrderItemEntity> orderItemEntities,
            List<OrderItemUpdater> orderItemUpdaters) {

        if (orderItemUpdaters == null) {
            if (orderItemEntities != null) {
                orderItemEntities.clear();
            }
            return;
        }
        List<Long> itemIds = orderItemUpdaters.stream()
                .map(OrderItemUpdater::getItemId)
                .toList();
        Map<Long, ItemEntity> itemMap = itemRepository.findAllById(itemIds).stream()
                .collect(Collectors.toMap(ItemEntity::getId, item -> item));
        orderItemEntities.clear();
        for (OrderItemUpdater updater : orderItemUpdaters) {
            OrderItemEntity entity = updateEntity(updater);
            entity.setItem(itemMap.get(updater.getItemId()));
            orderItemEntities.add(entity);
        }
    }

    public List<OrderItemEntity> dtoListEntity(List<OrderItemMaker> orderItemMakers){
        List<OrderItemEntity> orderItemEntities = new ArrayList<>();
        List<Long> itemIds = orderItemMakers.stream()
                .map(OrderItemMaker::getItemId)
                .toList();
        Map<Long, ItemEntity> itemMap = itemRepository.findAllById(itemIds).stream()
                .collect(Collectors.toMap(ItemEntity::getId, item -> item));
        for (OrderItemMaker maker : orderItemMakers) {
            OrderItemEntity entity = dtoToEntity(maker);
            entity.setItem(itemMap.get(maker.getItemId()));
            orderItemEntities.add(entity);
        }
        return orderItemEntities;
    }


}
