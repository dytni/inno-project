package by.dytni.innoviseproject.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import by.dytni.innoviseproject.repository.entity.CardEntity;

@Repository
public interface CardRepository extends JpaRepository<CardEntity, Long> {

    Page<CardEntity> findAll(Specification<CardEntity> spec, Pageable pageable);

    Page<CardEntity> findAllByUserId(Long userId, Pageable pageable);


    @Modifying
    @Query("""
            UPDATE CardEntity c
            SET c.activeStatus = :status
            WHERE c.id = :id
            """)
    void changeCardStatus(@Param("id") Long id, @Param("status") Boolean status);

}
