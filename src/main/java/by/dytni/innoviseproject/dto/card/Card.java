package by.dytni.innoviseproject.dto.card;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Card {

    private Long id;
    private Long userId;
    private String holderName;
    private String cardNumber;
    private LocalDate expiryDate;
    private Boolean activeStatus;
}
