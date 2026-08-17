package by.dytni.orderservice.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import by.dytni.orderservice.repository.entity.OrderEntity;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    //TODO delete maybe
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
              UPDATE OrderEntity o
              SET o.deleted = true
              WHERE o.id = :orderId
              """)
    void deleteOrderById(@Param("orderId") Long orderId);

    Page<OrderEntity> findAll(Specification<OrderEntity> spec, Pageable pageable);

    Page<OrderEntity> getOrdersByUserId(Long userId, Pageable pageable);

    boolean existsByIdAndUserId(Long id, Long userId);
}
