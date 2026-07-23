package by.dytni.innoviseproject.repository.criteria;

import org.springframework.data.domain.Pageable;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CardCriteria {
    private String userFirstName;
    private String userLastName;
    private Pageable pageable;
}
