package by.dytni.innoviseproject.dto.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserFilter {
    private String firstName;
    private String lastName;

    @Builder.Default
    private Integer page = 0;
    @Builder.Default
    private Integer size = 10;

}
