package by.dytni.userservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.ReportingPolicy;

import by.dytni.userservice.dto.user.User;
import by.dytni.userservice.dto.user.UserMaker;
import by.dytni.userservice.dto.user.UserUpdater;
import by.dytni.userservice.repository.entity.UserEntity;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring",
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public abstract class UserMapper {

    public abstract UserEntity dtoToEntity(UserMaker userMaker);
    public abstract User entityToDto(UserEntity userEntity);

    @Mapping(target = "id", ignore = true)
    public abstract UserEntity updateEntity(@MappingTarget UserEntity userEntity, UserUpdater userUpdater);

}
