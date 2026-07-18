package by.dytni.innoviseproject.service;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import by.dytni.innoviseproject.dto.user.User;
import by.dytni.innoviseproject.dto.user.UserFilter;
import by.dytni.innoviseproject.dto.user.UserMaker;

@Service
public interface UserService {

     Long createUser(UserMaker userMaker);

     Page<User> getAllUsers(UserFilter filter);

     User getUserById(Long userId) ;

     Long changeStatus(Long userId) ;
}
