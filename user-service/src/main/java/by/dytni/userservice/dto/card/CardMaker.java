package by.dytni.userservice.dto.card;

import static by.dytni.userservice.UserServiceConstants.CARD_NUMBER_PATTERN;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CardMaker {

    @NotNull
    private Long userId;

    @NotBlank
    @Pattern(regexp = CARD_NUMBER_PATTERN)
    private String cardNumber;
}
