package by.dytni.auth.service.advice;

import java.time.LocalDate;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import by.dytni.auth.dto.ErrorMessage;
import by.dytni.auth.exception.UserAlreadyExist;
import by.dytni.auth.exception.UserBlockedException;
import by.dytni.auth.exception.UserNotFoundException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorMessage> handleBadCredentials(BadCredentialsException exception, WebRequest request) {
        ErrorMessage message = ErrorMessage.builder()
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .timestamp(LocalDate.now())
                .message(exception.getMessage())
                .description(request.getDescription(false).replace("uri=", ""))
                .build();
        return new  ResponseEntity<>(message, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ErrorMessage> handleBadCredentials(ExpiredJwtException exception, WebRequest request) {
        ErrorMessage message = ErrorMessage.builder()
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .timestamp(LocalDate.now())
                .message(exception.getMessage())
                .description(request.getDescription(false).replace("uri=", ""))
                .build();
        return new  ResponseEntity<>(message, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ErrorMessage> handleJwtException(JwtException exception, WebRequest request) {
        ErrorMessage message = ErrorMessage.builder()
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .timestamp(LocalDate.now())
                .message(exception.getMessage())
                .description(request.getDescription(false).replace("uri=", ""))
                .build();
        return  new  ResponseEntity<>(message, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UserAlreadyExist.class)
    public ResponseEntity<ErrorMessage> handleIllegalState(UserAlreadyExist exception, WebRequest request) {
        ErrorMessage message = ErrorMessage.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .timestamp(LocalDate.now())
                .message(exception.getMessage())
                .description(request.getDescription(false).replace("uri=", ""))
                .build();
        return  new  ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserBlockedException.class)
    public ResponseEntity<ErrorMessage> handleIllegalState(UserBlockedException exception, WebRequest request) {
        ErrorMessage message = ErrorMessage.builder()
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .timestamp(LocalDate.now())
                .message(exception.getMessage())
                .description(request.getDescription(false).replace("uri=", ""))
                .build();
        return  new  ResponseEntity<>(message, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleIllegalState(UserNotFoundException exception, WebRequest request) {
        ErrorMessage message = ErrorMessage.builder()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .timestamp(LocalDate.now())
                .message(exception.getMessage())
                .description(request.getDescription(false).replace("uri=", ""))
                .build();
        return  new  ResponseEntity<>(message, HttpStatus.NOT_FOUND);
    }

}
