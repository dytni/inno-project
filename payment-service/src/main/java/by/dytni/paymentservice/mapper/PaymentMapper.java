package by.dytni.paymentservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.ReportingPolicy;

import by.dytni.paymentservice.dto.Payment;
import by.dytni.paymentservice.dto.PaymentMaker;
import by.dytni.paymentservice.repository.entity.PaymentDocument;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring",
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public abstract class PaymentMapper {


    public abstract PaymentDocument dtoToEntity(PaymentMaker maker);
    public abstract Payment entityToDto(PaymentDocument document);

}
