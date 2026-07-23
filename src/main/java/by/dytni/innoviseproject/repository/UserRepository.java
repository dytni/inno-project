package by.dytni.innoviseproject.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import by.dytni.innoviseproject.repository.entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Page<UserEntity> findAll(Specification<UserEntity> spec, Pageable pageable);

    @Modifying
    @Query("""
            UPDATE UserEntity u
            SET u.activeStatus = :status
            WHERE u.id = :id
            """)
    void changeUserStatus(@Param("id") Long id, @Param("status") Boolean status);

    @Query(value = """
        SELECT
            u.user_id,
            u.user_first_name,
            u.user_last_name,
            u.user_birth_date,
            u.user_email,
            u.user_active_status,
            u.created_by,
            u.created_at,
            u.updated_by,
            u.updated_at
        FROM user_entity u
        WHERE u.user_id = :id""", nativeQuery = true)
    Optional<UserEntity> findByUserId(@Param("id") Long id);

    @Query("""
        SELECT u
        FROM UserEntity u
        LEFT JOIN FETCH u.cards
        WHERE u.id = :id""")
    Optional<UserEntity> findByIdWithCards(@Param("id") Long id);

}
