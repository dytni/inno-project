package by.dytni.orderservice.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;


import by.dytni.orderservice.repository.entity.OrderEntity;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    Page<OrderEntity> findAll(Specification<OrderEntity> spec, Pageable pageable);

    Page<OrderEntity> getOrdersByUserId(Long userId, Pageable pageable);

    boolean existsByIdAndUserId(Long id, Long userId);
}
