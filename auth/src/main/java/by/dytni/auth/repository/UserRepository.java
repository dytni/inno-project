package by.dytni.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import by.dytni.auth.repository.model.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    boolean existsByLogin(String login);

    Optional<UserEntity> findByLogin(String login);
}
