package by.dytni.orderservice.repository.specifications;

import java.time.LocalDateTime;
import java.util.Collection;

import org.springframework.data.jpa.domain.Specification;

import by.dytni.commonhibernate.repository.entity.AuditableEntity;
import by.dytni.orderservice.repository.criteria.OrderCriteria;
import by.dytni.orderservice.repository.entity.OrderEntity;
import by.dytni.orderservice.repository.entity.OrderStatus;

public class OrderSpecification {

    public static Specification<OrderEntity> getSpecification(OrderCriteria criteria) {
        return Specification.where(notDeleted(criteria.getShowDeleted()))
                .and(hasStatuses(criteria.getStatuses()))
                .and(createdAtBetween(criteria.getFrom(), criteria.getTo()));
    }

    public static Specification<OrderEntity> createdAtBetween(LocalDateTime from, LocalDateTime to) {
        return (root, query, cb) -> {
            if (from == null && to == null) {
                return null;
            }
            if (from == null) {
                return cb.lessThanOrEqualTo(
                        root.get(AuditableEntity.Fields.createdAt),
                        to
                );
            }

            if (to == null) {
                return cb.greaterThanOrEqualTo(
                        root.get(AuditableEntity.Fields.createdAt),
                        from
                );
            }

            return cb.between(
                    root.get(AuditableEntity.Fields.createdAt),
                    from,
                    to
            );
        };
    }

    public static Specification<OrderEntity> hasStatuses(Collection<OrderStatus> statuses) {
        return (root, query, cb) -> {

            if (statuses == null || statuses.isEmpty()) {
                return null;
            }

            return root.get(OrderEntity.Fields.status).in(statuses);
        };
    }

    public static Specification<OrderEntity> notDeleted(Boolean showDeleted) {
        if (showDeleted == null) return null;
        if (showDeleted) return null;
        return (root, query, cb) ->
                cb.isFalse(root.get(OrderEntity.Fields.deleted));
    }
}
