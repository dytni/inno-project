package by.dytni.orderservice.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import by.dytni.orderservice.dto.order.OrderFilter;
import by.dytni.orderservice.repository.criteria.OrderCriteria;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring",
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public abstract class OrderCriteriaMapper {
    public abstract OrderCriteria dtoToCriteria(OrderFilter filter);

    @AfterMapping
    protected void populateCriteria(OrderFilter filter, @MappingTarget OrderCriteria criteria) {
        Pageable pageable =
                PageRequest.of(filter.getPage(),
                               filter.getSize());
        criteria.setPageable(pageable);
    }
}
