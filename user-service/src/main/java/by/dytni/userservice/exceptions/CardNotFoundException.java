package by.dytni.userservice.exceptions;

import static by.dytni.userservice.UserServiceConstants.CARD_NOT_FOUND_ERROR;

public class CardNotFoundException extends RuntimeException {
    public CardNotFoundException(Long id) {
        super(String.format(CARD_NOT_FOUND_ERROR, id));
    }
}
