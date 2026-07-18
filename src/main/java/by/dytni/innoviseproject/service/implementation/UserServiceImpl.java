package by.dytni.innoviseproject.service.implementation;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import by.dytni.innoviseproject.dto.user.User;
import by.dytni.innoviseproject.dto.user.UserFilter;
import by.dytni.innoviseproject.dto.user.UserMaker;
import by.dytni.innoviseproject.mapper.UserCriteriaMapper;
import by.dytni.innoviseproject.mapper.UserMapper;
import by.dytni.innoviseproject.repository.UserRepository;
import by.dytni.innoviseproject.repository.criteria.UserCriteria;
import by.dytni.innoviseproject.repository.entity.UserEntity;
import by.dytni.innoviseproject.repository.specification.UserSpecification;
import by.dytni.innoviseproject.service.UserService;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserCriteriaMapper userCriteriaMapper;

    @Override
    @Transactional
    public Long createUser(UserMaker userMaker) {
        UserEntity userEntity = userMapper.dtoToEntity(userMaker);
        userRepository.save(userEntity);
        return userEntity.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<User> getAllUsers(UserFilter filter) {
        UserCriteria criteria = userCriteriaMapper.dtoToCriteria(filter);
        Specification<UserEntity> spec = UserSpecification.getSpecification(criteria);
        return userRepository.findAll(spec, criteria.getPageable())
                .map(entity -> userMapper.entityToDto(entity));
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserById(Long userId) {
        return userRepository.findByUserId(userId)
                .map(entity -> userMapper.entityToDto(entity)).orElseThrow();
    }

    @Override
    @Transactional
    public Long changeStatus(Long userId) {
        UserEntity userEntity = userRepository.findByUserId(userId).orElseThrow();
        userEntity.setActiveStatus(!userEntity.getActiveStatus());
        userRepository.save(userEntity);
        return userEntity.getId();
    }
}
