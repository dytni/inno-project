package by.dytni.innoviseproject.repository;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import by.dytni.innoviseproject.repository.entity.CardEntity;

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


    @Query(value = """
        SELECT
            c.card_id,
            c.card_active_status,
            c.user_id
        FROM card_entity c
        WHERE c.user_id = :userId""", nativeQuery = true)
    Collection<CardEntity> findByUserId(@Param("userId") Long userId);
}
