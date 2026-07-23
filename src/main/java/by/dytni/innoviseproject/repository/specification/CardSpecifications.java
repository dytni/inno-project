package by.dytni.innoviseproject.repository.specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import by.dytni.innoviseproject.repository.criteria.CardCriteria;
import by.dytni.innoviseproject.repository.entity.CardEntity;
import by.dytni.innoviseproject.repository.entity.UserEntity;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;

@Component
public class CardSpecifications {
    public Specification<CardEntity> getSpecification(CardCriteria criteria) {

        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            Join<CardEntity, UserEntity> userJoin = root.join(CardEntity.Fields.user);
            if (StringUtils.isNotBlank(criteria.getUserFirstName()))
                predicates.add(builder.equal(userJoin.get(UserEntity.Fields.firstName), criteria.getUserFirstName()));

            if (StringUtils.isNotBlank(criteria.getUserLastName()))
                predicates.add(builder.equal(userJoin.get(UserEntity.Fields.lastName), criteria.getUserLastName()));

            return builder.and(predicates.toArray(Predicate[]::new));
        };
    }

}
