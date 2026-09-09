package by.dytni.paymentservice.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import by.dytni.paymentservice.dto.PaymentFilter;
import by.dytni.paymentservice.repository.criteria.PaymentCriteria;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring",
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public abstract class PaymentCriteriaMapper {

    public abstract PaymentCriteria filterToCriteria(PaymentFilter paymentFilter);

    @AfterMapping
    protected void populateCriteria(PaymentFilter filter, @MappingTarget PaymentCriteria criteria) {
        Pageable pageable =
                PageRequest.of(filter.getPage(),
                               filter.getSize());
        criteria.setPageable(pageable);
    }
}
