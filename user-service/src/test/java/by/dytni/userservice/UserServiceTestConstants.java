package by.dytni.userservice;

import java.time.LocalDate;
import java.time.Month;

import lombok.experimental.UtilityClass;

@UtilityClass
public class UserServiceTestConstants {
    public static final String CREATED_BY = "999";
    public static final Long USER_ID = 1L;
    public static final Boolean USER_ACTIVE = true;
    public static final Boolean USER_DEACTIVE = false;
    public static final String USER_EMAIL = "test@gmail.com";
    public static final LocalDate USER_BIRTH_DATE = LocalDate.of(1990, Month.DECEMBER, 1);
    public static final String USER_FIRST_NAME = "test";
    public static final String USER_ANOTHER_FIRST_NAME = "test2";
    public static final String USER_LAST_NAME = "test";

    public static final Long CARD_ID = 1L;
    public static final Long CARD_ANOTHER_ID = 2L;
    public static final String CARD_HOLDER = "test test";
    public static final String CARD_NUMBER = "1234567890123456";
    public static final String CARD_ANOTHER_NUMBER = "1234567890123457";
    public static final LocalDate CARD_EXPIRY_DATE = LocalDate.of(2031, Month.DECEMBER, 1);
    public static final Boolean CARD_ACTIVE = true;
    public static final Boolean CARD_DEACTIVE = false;
}
