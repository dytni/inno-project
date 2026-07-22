package by.dytni.innoviseproject;

import lombok.experimental.UtilityClass;

@UtilityClass
public class InnoviseProjectConstants {
    public static final String CARD_NOT_FOUND_ERROR = "Card with id: %s not found";
    public static final String USER_NOT_FOUND_ERROR = "User with id: %s not found";
    public static final String CARDS_LIMIT_ERROR = "One user can have only 5 cards";
    public static final String BUSINESS_LOGIC_ERROR = "One user can have only 5 cards";


    public final static String CARD_NUMBER_PATTERN = "\\d{16}";

}
