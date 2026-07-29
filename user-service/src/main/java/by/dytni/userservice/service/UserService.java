package by.dytni.userservice.service;

import org.springframework.data.domain.Page;

import by.dytni.userservice.dto.user.User;
import by.dytni.userservice.dto.user.UserFilter;
import by.dytni.userservice.dto.user.UserMaker;
import by.dytni.userservice.dto.user.UserUpdater;

public interface UserService {

     User createUser(UserMaker userMaker);

     User updateUser(UserUpdater userUpdater, Long userId);

     User deleteUser( Long userId);

     Page<User> getAllUsers(UserFilter filter);

     User getUserById(Long userId);

     User changeStatus(Long userId);
}
