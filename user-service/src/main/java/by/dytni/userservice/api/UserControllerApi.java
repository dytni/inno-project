package by.dytni.userservice.api;


import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import by.dytni.userservice.dto.user.User;
import by.dytni.userservice.dto.user.UserMaker;
import by.dytni.userservice.dto.user.UserUpdater;

public interface UserControllerApi {
    ResponseEntity<User> createUser(UserMaker userMaker);
    ResponseEntity<User> updateUser(UserUpdater userUpdater, Long id);
    ResponseEntity<User> deleteUser(Long id);
    ResponseEntity<Page<User>> getAllUsers(String name, String lastName, int page, int size);
    ResponseEntity<User> getUserById(Long id);
    ResponseEntity<User> changeStatus(Long id);
}
