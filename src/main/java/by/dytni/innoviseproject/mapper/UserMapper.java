package by.dytni.innoviseproject.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.ReportingPolicy;

import by.dytni.innoviseproject.dto.user.User;
import by.dytni.innoviseproject.dto.user.UserMaker;
import by.dytni.innoviseproject.repository.entity.UserEntity;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring",
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public abstract class UserMapper {

    public abstract UserEntity dtoToEntity(UserMaker userMaker);
    public abstract User entityToDto(UserEntity userEntity);

}
