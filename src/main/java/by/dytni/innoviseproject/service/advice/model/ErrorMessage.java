package by.dytni.innoviseproject.service.advice.model;



import java.time.LocalDate;
import java.util.Date;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorMessage {

    private int statusCode;
    private LocalDate timestamp;
    private String message;
    private String description;

}
