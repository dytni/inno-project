package by.dytni.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import by.dytni.auth.repository.model.Role;
import by.dytni.auth.repository.model.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    boolean existsByLogin(String login);

    Optional<UserEntity> findByLogin(String login);

    @Modifying
    @Query("""
            UPDATE UserEntity u
            SET u.activeStatus = :status
            WHERE u.login = :login
            """)
    void changeUserStatus(@Param("login") String login, @Param("status") Boolean status);

    @Modifying
    @Query("""
        UPDATE UserEntity u
        SET u.role = :role
        WHERE u.login = :login
        """)
    void changeRole(@Param("login") String login, @Param("role") Role role);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
            UPDATE UserEntity u
            SET u.login = :newLogin
            WHERE u.login = :login
            """)
    void changeUserLogin(@Param("login") String login, @Param("newLogin") String newLogin);
}
