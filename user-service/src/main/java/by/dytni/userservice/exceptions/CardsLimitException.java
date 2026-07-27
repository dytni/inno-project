package by.dytni.userservice.exceptions;

public class CardsLimitException extends RuntimeException {
    public CardsLimitException(String message) {
        super(message);
    }
}
