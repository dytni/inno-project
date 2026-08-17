package by.dytni.orderservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.ReportingPolicy;

import by.dytni.orderservice.dto.item.Item;
import by.dytni.orderservice.dto.item.ItemMaker;
import by.dytni.orderservice.dto.item.ItemUpdater;
import by.dytni.orderservice.repository.entity.ItemEntity;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring",
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public abstract class ItemMapper {

    public abstract Item entityToDto(ItemEntity itemEntity);


    @Mapping(target = "id" , ignore = true)
    public abstract ItemEntity dtoToEntity(ItemMaker itemMaker);

    public abstract ItemEntity updateEntity(@MappingTarget ItemEntity entity, ItemUpdater updater);

}
