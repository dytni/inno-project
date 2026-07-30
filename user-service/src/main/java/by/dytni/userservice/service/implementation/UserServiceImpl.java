package by.dytni.userservice.service.implementation;

import static by.dytni.userservice.UserServiceConstants.KAFKA_USER_STATUS_TOPIC;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import by.dytni.commonevents.Producer;
import by.dytni.commonevents.dto.UserStatusChangedEvent;
import by.dytni.userservice.dto.user.User;
import by.dytni.userservice.dto.user.UserFilter;
import by.dytni.userservice.dto.user.UserMaker;
import by.dytni.userservice.dto.user.UserUpdater;
import by.dytni.userservice.exceptions.UserNotFoundException;
import by.dytni.userservice.mapper.UserCriteriaMapper;
import by.dytni.userservice.mapper.UserMapper;
import by.dytni.userservice.repository.UserRepository;
import by.dytni.userservice.repository.criteria.UserCriteria;
import by.dytni.userservice.repository.entity.UserEntity;
import by.dytni.userservice.repository.specification.UserSpecification;
import by.dytni.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserCriteriaMapper userCriteriaMapper;
    private final UserSpecification userSpecification;
    private final Producer producer;

    @Override
    @Transactional
    @CachePut(value = "users", key = "#result.id")
    public User createUser(UserMaker userMaker) {
        log.info("Create user: {}", userMaker);
        UserEntity userEntity = userMapper.dtoToEntity(userMaker);
        userRepository.save(userEntity);
        return userMapper.entityToDto(userEntity);
    }

    @Override
    @Transactional
    @CacheEvict(value = "users", key = "#userId")
    public User updateUser(UserUpdater userUpdater, Long userId) {
        log.info("Update user: {}", userUpdater);
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        userRepository.save(userMapper.updateEntity(userEntity, userUpdater));
        return userMapper.entityToDto(userEntity);
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "users", key = "#userId"),
            @CacheEvict(value = "cardsByUserId", key = "#userId")
    })
    public User deleteUser(Long userId) {
        log.info("Delete user: {}", userId);
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        userRepository.delete(userEntity);
        return userMapper.entityToDto(userEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<User> getAllUsers(UserFilter filter) {
        log.info("Get all users: {}", filter);
        UserCriteria criteria = userCriteriaMapper.dtoToCriteria(filter);
        Specification<UserEntity> spec = userSpecification.getSpecification(criteria);
        return userRepository.findAll(spec, criteria.getPageable())
                .map(userMapper::entityToDto);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "users", key = "#userId")
    public User getUserById(Long userId) {
        log.info("Get user: {}", userId);
        return userRepository.findByUserId(userId)
                .map(userMapper::entityToDto)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }


@Override
@Transactional
@CacheEvict(value = "users", key = "#userId")
public User changeStatus(Long userId) {
    log.info("Change user status: {}", userId);
    UserEntity userEntity = userRepository.findByUserId(userId)
            .orElseThrow(() -> new UserNotFoundException(userId));
    boolean newStatus = !userEntity.getActiveStatus();
    userRepository.changeUserStatus(userId, newStatus);
    producer.send(
            new UserStatusChangedEvent(userEntity.getEmail(), newStatus),
            KAFKA_USER_STATUS_TOPIC
    );
    userEntity.setActiveStatus(newStatus);
    return userMapper.entityToDto(userEntity);
}
}