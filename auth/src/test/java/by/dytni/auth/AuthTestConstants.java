package by.dytni.auth;

import by.dytni.auth.repository.model.Role;
import lombok.experimental.UtilityClass;

@UtilityClass
public class AuthTestConstants {
    public static final String TEST_USER_PASSWORD = "test_password";
    public static final String TEST_ADMIN_LOGIN = "admin";
    public static final String TEST_ADMIN_PASSWORD = "1111";
    public static final String TEST_ADMIN_WRONG_PASSWORD = "1234";
    public static final Role TEST_USER_ROLE = Role.USER;
}
