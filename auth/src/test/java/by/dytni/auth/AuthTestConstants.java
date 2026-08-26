package by.dytni.auth;

import java.time.LocalDate;
import java.time.Month;

import lombok.experimental.UtilityClass;

@UtilityClass
public class AuthTestConstants {

    public static final LocalDate USER_BIRTH_DATE = LocalDate.of(1990, Month.DECEMBER, 1);
    public static final String USER_FIRST_NAME = "test";
    public static final String USER_LAST_NAME = "test";
    public static final String TEST_USER_PASSWORD = "test_password";
    public static final String TEST_ADMIN_LOGIN = "admin";
    public static final String TEST_ADMIN_PASSWORD = "1111";
    public static final String TEST_ADMIN_WRONG_PASSWORD = "1234";
}
