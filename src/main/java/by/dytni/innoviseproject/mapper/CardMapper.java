package by.dytni.innoviseproject.mapper;

import java.time.LocalDate;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.ReportingPolicy;

import by.dytni.innoviseproject.dto.card.Card;
import by.dytni.innoviseproject.dto.card.CardMaker;
import by.dytni.innoviseproject.dto.card.CardUpdater;
import by.dytni.innoviseproject.repository.entity.CardEntity;
import by.dytni.innoviseproject.repository.entity.UserEntity;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring",
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public abstract class CardMapper {

    @Mapping(target = "expiryDate", expression = "java(setExpiryDate())")
    @Mapping(target = "holderName", expression = "java(setHolderName(user))")
    @Mapping(target = "user", source = "user")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "activeStatus", ignore = true)
    public abstract CardEntity dtoToEntity(CardMaker cardMaker, UserEntity user);


    @Mapping(target = "userId" , source = "user.id")
    public abstract Card entityToDto(CardEntity cardEntity);

    @Mapping(target = "id", ignore = true)
    public abstract CardEntity updateEntity(@MappingTarget CardEntity cardEntity, CardUpdater cardUpdater);

    @Named("setExpiryDate")
    protected LocalDate setExpiryDate() {
        return LocalDate.now().plusYears(5);
    }
    @Named("setHolderName")
    protected String setHolderName(UserEntity userEntity) {
        return userEntity.getFirstName() + " " + userEntity.getLastName();
    }
}
