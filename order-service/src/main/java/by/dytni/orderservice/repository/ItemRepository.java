package by.dytni.orderservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import by.dytni.orderservice.repository.entity.ItemEntity;

public interface ItemRepository extends JpaRepository<ItemEntity, Long> {
}
