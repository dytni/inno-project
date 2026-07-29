package by.dytni.auth.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.ReportingPolicy;

import by.dytni.auth.dto.RegisterRequest;
import by.dytni.auth.repository.model.UserEntity;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring",
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public abstract class UserMapper {
    public abstract UserEntity dtoToEntity(RegisterRequest registerRequest);
}
