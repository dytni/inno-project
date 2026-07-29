package by.dytni.auth;

import by.dytni.auth.repository.model.Role;
import lombok.experimental.UtilityClass;

@UtilityClass
public class AuthTestConstants {
    public static final String TEST_USER_LOGIN = "test_user";
    public static final String TEST_USER_PASSWORD = "test_password";
    public static final Role TEST_USER_ROLE = Role.USER;
    public static final Role TEST_ADMIN_ROLE = Role.ADMIN;
}
