package by.dytni.auth.exception;

import static by.dytni.auth.AuthConstant.USER_NOT_FOUND_ID_ERROR;
import static by.dytni.auth.AuthConstant.USER_NOT_FOUND_LOGIN_ERROR;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long id) {
        super(String.format(USER_NOT_FOUND_ID_ERROR, id));

    }
    public UserNotFoundException(String login) {
        super(String.format(USER_NOT_FOUND_LOGIN_ERROR, login));
    }
}
