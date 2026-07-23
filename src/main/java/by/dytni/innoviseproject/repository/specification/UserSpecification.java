package by.dytni.innoviseproject.repository.specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import by.dytni.innoviseproject.repository.criteria.UserCriteria;
import by.dytni.innoviseproject.repository.entity.UserEntity;

import io.micrometer.common.util.StringUtils;
import jakarta.persistence.criteria.Predicate;

@Component
public class UserSpecification {
    public Specification<UserEntity> getSpecification(UserCriteria criteria) {

        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.isNotBlank(criteria.getFirstName()))
                predicates.add(builder.equal(root.get(UserEntity.Fields.firstName), criteria.getFirstName()));

            if (StringUtils.isNotBlank(criteria.getLastName()))
                predicates.add(builder.equal(root.get(UserEntity.Fields.lastName), criteria.getLastName()));

            return builder.and(predicates.toArray(Predicate[]::new));
        };
    }
}
