package by.dytni.innoviseproject.service.advice;

import static by.dytni.innoviseproject.InnoviseProjectConstants.BUSINESS_LOGIC_ERROR;

import java.time.LocalDate;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import by.dytni.innoviseproject.exceptions.CardNotFoundException;
import by.dytni.innoviseproject.exceptions.CardsLimitException;
import by.dytni.innoviseproject.exceptions.UserNotFoundException;
import by.dytni.innoviseproject.service.advice.model.ErrorMessage;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            UserNotFoundException.class,
            CardNotFoundException.class
    })
    public ResponseEntity<ErrorMessage> handleResourceNotFoundException(RuntimeException ex, WebRequest request) {

        ErrorMessage message = ErrorMessage.builder()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .timestamp(LocalDate.now())
                .message(ex.getMessage())
                .description(request.getDescription(false).replace("uri=", ""))
                .build();

        return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(CardsLimitException.class)
    public ResponseEntity<ErrorMessage> handleAccessDeniedException(RuntimeException ex, WebRequest request) {

        ErrorMessage message = ErrorMessage.builder()
                .statusCode(HttpStatus.CONFLICT.value())
                .timestamp(LocalDate.now())
                .message(BUSINESS_LOGIC_ERROR + ex.getMessage())
                .description(request.getDescription(false).replace("uri=", ""))
                .build();

        return new ResponseEntity<>(message, HttpStatus.CONFLICT);
    }
}
