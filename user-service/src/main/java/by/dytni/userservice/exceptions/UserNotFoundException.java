package by.dytni.userservice.exceptions;

import static by.dytni.userservice.UserServiceConstants.USER_NOT_FOUND_ERROR;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long id) {
        super(String.format(USER_NOT_FOUND_ERROR, id));
    }
}
