package by.dytni.auth;

import lombok.experimental.UtilityClass;

@UtilityClass
public class AuthConstant {



    public static final String KAFKA_USER_STATUS_TOPIC = "user-status";

    public static final String ROLE_CLAIM = "role";
    public static final String INVALID_LOGIN_ERROR = "Invalid login";
    public static final String USER_BLOCKED_ERROR = "User blocked error";
    public static final String INVALID_PASSWORD_ERROR = "Invalid password";
    public static final String INVALID_TOKEN_ERROR = "Invalid token";
    public static final String USER_NOT_FOUND_ERROR = "User with id: %s not found";
    public static final String INVALID_REFRESH_TOKEN_ERROR = "Invalid refresh token";
    public static final String LOGIN_ALREADY_EXIST_ERROR ="Login already exists";
}
