package by.dytni.innoviseproject;

import java.time.LocalDate;
import java.util.UUID;

import lombok.experimental.UtilityClass;

@UtilityClass
public class InnoviseProjectConstantsTest {
    public static Long USER_ID = 1L;
    public static Boolean USER_ACTIVE = true;
    public static Boolean USER_DEACTIVE = false;
    public static String USER_EMAIL = "test@gmail.com";
    public static LocalDate USER_BIRTH_DATE = LocalDate.of(1990, 1, 1);
    public static String USER_FIRST_NAME = "test";
    public static String USER_ANOTHER_FIRST_NAME = "test2";
    public static String USER_LAST_NAME = "test";

    public static Long CARD_ID = 1L;
    public static Long CARD_ANOTHER_ID = 2L;
    public static String CARD_HOLDER = "test test";
    public static String CARD_NUMBER = "1234567890123456";
    public static String CARD_ANOTHER_NUMBER = "1234567890123457";
    public static LocalDate CARD_EXPIRY_DATE = LocalDate.of(2031, 1, 1);
    public static Boolean CARD_ACTIVE = true;
    public static Boolean CARD_DEACTIVE = false;
}
