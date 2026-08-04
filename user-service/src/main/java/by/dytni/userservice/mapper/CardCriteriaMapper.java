package by.dytni.userservice.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import by.dytni.userservice.dto.card.CardFilter;
import by.dytni.userservice.repository.criteria.CardCriteria;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring",
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public abstract class CardCriteriaMapper {


    public abstract CardCriteria dtoToCriteria(CardFilter filter);

    @AfterMapping
    protected void populateCriteria(CardFilter filter, @MappingTarget CardCriteria criteria) {
        Pageable pageable =
                PageRequest.of(filter.getPage(),
                               filter.getSize());
        criteria.setPageable(pageable);
    }
}