package by.dytni.innoviseproject.service;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import by.dytni.innoviseproject.dto.user.User;
import by.dytni.innoviseproject.dto.user.UserFilter;
import by.dytni.innoviseproject.dto.user.UserMaker;
import by.dytni.innoviseproject.dto.user.UserUpdater;

@Service
public interface UserService {

     User createUser(UserMaker userMaker);

     User updateUser(UserUpdater userUpdater, Long userId);

     User deleteUser( Long userId);

     Page<User> getAllUsers(UserFilter filter);

     User getUserById(Long userId) ;

     User changeStatus(Long userId) ;
}
