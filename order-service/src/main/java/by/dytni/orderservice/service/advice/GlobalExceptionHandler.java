package by.dytni.orderservice.service.advice;


import static by.dytni.orderservice.OrderServiceConstants.BUSINESS_LOGIC_ERROR;

import java.time.LocalDate;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import by.dytni.orderservice.dto.ErrorMessage;
import by.dytni.orderservice.exceptions.ItemNotFoundException;
import by.dytni.orderservice.exceptions.OrderNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            OrderNotFoundException.class,
            ItemNotFoundException.class
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
