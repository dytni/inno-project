package by.dytni.innoviseproject.dto.user;

import lombok.Data;

@Data
public class User {
    private Long id;
    private String firstName;
    private String lastName;
    private Boolean activeStatus;
}
