package by.dytni.innoviseproject.dto.card;

import static by.dytni.innoviseproject.InnoviseProjectConstants.CARD_NUMBER_PATTERN;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CardUpdater {

    @NotBlank
    @Pattern(regexp = CARD_NUMBER_PATTERN)
    private String cardNumber;
}
