package by.dytni.innoviseproject.repository.criteria;

import org.springframework.data.domain.Pageable;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserCriteria {
    private String firstName;
    private String lastName;
    private Pageable pageable;

}
