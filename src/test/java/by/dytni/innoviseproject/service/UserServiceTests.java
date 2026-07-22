package by.dytni.innoviseproject.service;

import static by.dytni.innoviseproject.InnoviseProjectConstantsTest.USER_ACTIVE;
import static by.dytni.innoviseproject.InnoviseProjectConstantsTest.USER_ANOTHER_FIRST_NAME;
import static by.dytni.innoviseproject.InnoviseProjectConstantsTest.USER_BIRTH_DATE;
import static by.dytni.innoviseproject.InnoviseProjectConstantsTest.USER_DEACTIVE;
import static by.dytni.innoviseproject.InnoviseProjectConstantsTest.USER_EMAIL;
import static by.dytni.innoviseproject.InnoviseProjectConstantsTest.USER_FIRST_NAME;
import static by.dytni.innoviseproject.InnoviseProjectConstantsTest.USER_ID;
import static by.dytni.innoviseproject.InnoviseProjectConstantsTest.USER_LAST_NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import by.dytni.innoviseproject.dto.user.User;
import by.dytni.innoviseproject.dto.user.UserFilter;
import by.dytni.innoviseproject.dto.user.UserMaker;
import by.dytni.innoviseproject.dto.user.UserUpdater;
import by.dytni.innoviseproject.mapper.UserCriteriaMapper;
import by.dytni.innoviseproject.mapper.UserMapper;
import by.dytni.innoviseproject.repository.UserRepository;
import by.dytni.innoviseproject.repository.criteria.UserCriteria;
import by.dytni.innoviseproject.repository.entity.CardEntity;
import by.dytni.innoviseproject.repository.entity.UserEntity;
import by.dytni.innoviseproject.repository.specification.UserSpecification;
import by.dytni.innoviseproject.service.implementation.UserServiceImpl;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private UserCriteriaMapper criteriaMapper;
    @Mock
    private UserSpecification userSpecification;
    @Mock
    Specification<UserEntity> specification;

    @InjectMocks
    UserServiceImpl service;

    private UserEntity userEntity;
    private User user;

    @BeforeEach
    void setUp() {

        CardEntity cardEntity = CardEntity.builder().user(userEntity).build();
        List<CardEntity> cardEntities = List.of(cardEntity);
        userEntity = UserEntity.builder()
                .id(USER_ID)
                .activeStatus(USER_ACTIVE)
                .email(USER_EMAIL)
                .birthDate(USER_BIRTH_DATE)
                .firstName(USER_FIRST_NAME)
                .lastName(USER_LAST_NAME)
                .cards(cardEntities)
                .build();
        user = User.builder()
                .id(USER_ID)
                .activeStatus(USER_ACTIVE)
                .email(USER_EMAIL)
                .birthDate(USER_BIRTH_DATE)
                .firstName(USER_FIRST_NAME)
                .lastName(USER_LAST_NAME)
                .build();

    }


    @AfterEach
    void complete() {
        verifyNoMoreInteractions(userRepository, userMapper, criteriaMapper, specification, userSpecification);
    }

    @Test
    public void get_all_users() {
        UserFilter userFilter = UserFilter.builder()
                .page(0)
                .size(10)
                .build();
        Pageable pageable = PageRequest.of(userFilter.getPage(), userFilter.getSize());
        Page<UserEntity> userEntityPage = new PageImpl<>(List.of(userEntity, userEntity, userEntity), pageable, 3);
        UserCriteria userCriteria = UserCriteria.builder()
                .pageable(pageable)
                .build();


        when(criteriaMapper.dtoToCriteria(userFilter)).thenReturn(userCriteria);
        when(userSpecification.getSpecification(userCriteria)).thenReturn(specification);
        when(userRepository.findAll(specification, pageable)).thenReturn(userEntityPage);
        when(userMapper.entityToDto(userEntity)).thenReturn(user);

        Page<User> result = service.getAllUsers(userFilter);
        assertThat(result).hasSize(3);
        assertThat(result.getContent().getFirst()).isEqualTo(user);

        verify(criteriaMapper, times(1)).dtoToCriteria(userFilter);
        verify(userSpecification, times(1)).getSpecification(userCriteria);
        verify(userRepository, times(1)).findAll(specification, pageable);
        verify(userMapper, times(3)).entityToDto(userEntity);

    }

    @Test
    void get_user_by_id() {
        when(userRepository.findByUserId(USER_ID)).thenReturn(Optional.of(userEntity));
        when(userMapper.entityToDto(userEntity)).thenReturn(user);
        User result = service.getUserById(USER_ID);
        assertThat(result).isEqualTo(user);
        verify(userRepository, times(1)).findByUserId(USER_ID);
        verify(userMapper, times(1)).entityToDto(userEntity);

    }

    @Test
    void create_user() {
        UserMaker userMaker = UserMaker.builder()
                .firstName(USER_FIRST_NAME)
                .lastName(USER_LAST_NAME)
                .email(USER_EMAIL)
                .birthDate(USER_BIRTH_DATE)
                .build();
        when(userMapper.dtoToEntity(userMaker)).thenReturn(userEntity);
        when(userRepository.save(userEntity)).thenReturn(userEntity);
        when(userMapper.entityToDto(userEntity)).thenReturn(user);

        User result = service.createUser(userMaker);
        assertThat(result).isEqualTo(user);

        verify(userRepository, times(1)).save(userEntity);
    }

    @Test
    void update_user() {
        UserEntity changedUserEntity = UserEntity.builder()
                .id(USER_ID)
                .firstName(USER_ANOTHER_FIRST_NAME)
                .build();
        User changedUser = User.builder()
                .id(USER_ID)
                .firstName(USER_ANOTHER_FIRST_NAME)
                .build();
        UserUpdater userUpdater = UserUpdater.builder()
                .firstName(USER_ANOTHER_FIRST_NAME)
                .build();

        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(userEntity));
        when(userMapper.updateEntity(userEntity, userUpdater)).thenReturn(changedUserEntity);
        when(userRepository.save(changedUserEntity)).thenReturn(changedUserEntity);
        when(userMapper.entityToDto(userEntity)).thenReturn(changedUser);

        User result = service.updateUser(userUpdater, USER_ID);
        assertThat(result).isEqualTo(changedUser);

        verify(userRepository, times(1)).findById(USER_ID);
        verify(userMapper, times(1)).updateEntity(userEntity, userUpdater);
        verify(userRepository, times(1)).save(changedUserEntity);
        verify(userMapper, times(1)).entityToDto(userEntity);

    }

    @Test
    void delete_user() {

        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(userEntity));
        when(userMapper.entityToDto(userEntity)).thenReturn(user);

        service.deleteUser(USER_ID);

        verify(userRepository, times(1)).findById(USER_ID);
        verify(userRepository, times(1)).delete(userEntity);
    }

    @Test
    void change_user_status() {
        User changedUser = User.builder()
                .id(USER_ID)
                .firstName(USER_FIRST_NAME)
                .lastName(USER_LAST_NAME)
                .email(USER_EMAIL)
                .birthDate(USER_BIRTH_DATE)
                .activeStatus(USER_DEACTIVE)
                .build();

        when(userRepository.findByUserId(USER_ID)).thenReturn(Optional.of(userEntity));
        when(userMapper.entityToDto(userEntity)).thenReturn(changedUser);

        User result = service.changeStatus(USER_ID);
        assertThat(result).isEqualTo(changedUser);

        verify(userRepository, times(1)).findByUserId(USER_ID);
        verify(userRepository, times(1)).changeUserStatus(USER_ID, USER_DEACTIVE);
        verify(userMapper, times(1)).entityToDto(userEntity);
    }


}
