package by.dytni.userservice.service.advice;

import static by.dytni.userservice.UserServiceConstants.BUSINESS_LOGIC_ERROR;

import java.time.LocalDate;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import by.dytni.userservice.exceptions.CardNotFoundException;
import by.dytni.userservice.exceptions.CardsLimitException;
import by.dytni.userservice.exceptions.UserNotFoundException;
import by.dytni.userservice.dto.ErrorMessage;

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
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> handleAccessDeniedException(Exception ex, WebRequest request) {

        ErrorMessage message = ErrorMessage.builder()
                .statusCode(HttpStatus.EXPECTATION_FAILED.value())
                .timestamp(LocalDate.now())
                .message(BUSINESS_LOGIC_ERROR + ex.getMessage())
                .description(request.getDescription(false).replace("uri=", ""))
                .build();

        return new ResponseEntity<>(message, HttpStatus.EXPECTATION_FAILED);
    }
}
