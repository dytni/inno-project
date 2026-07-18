package by.dytni.innoviseproject.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import by.dytni.innoviseproject.dto.user.UserFilter;
import by.dytni.innoviseproject.repository.criteria.UserCriteria;


@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring",
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public abstract class UserCriteriaMapper {


    public abstract UserCriteria dtoToCriteria(UserFilter filter);

    @AfterMapping
    protected void populateCriteria(UserFilter filter, @MappingTarget UserCriteria criteria) {
        Pageable pageable =
                PageRequest.of(filter.getPage(),
                               filter.getSize());
        criteria.setPageable(pageable);
    }
}
