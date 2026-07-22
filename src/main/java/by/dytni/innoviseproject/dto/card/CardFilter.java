package by.dytni.innoviseproject.dto.card;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CardFilter {

    private String userFirstName;
    private String userLastName;

    @Builder.Default
    private Integer page = 0;
    @Builder.Default
    private Integer size = 10;
}
