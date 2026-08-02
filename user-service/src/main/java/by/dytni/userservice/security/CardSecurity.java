package by.dytni.userservice.security;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import by.dytni.userservice.service.CardService;
import lombok.RequiredArgsConstructor;

@Component("cardSecurity")
@RequiredArgsConstructor
public class CardSecurity {

    private final CardService cardService;


    public boolean isOwner(Long cardId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication.getPrincipal() instanceof Long userId))
            return false;


        return cardService.existsByIdAndUserId(cardId, userId);
    }
}
