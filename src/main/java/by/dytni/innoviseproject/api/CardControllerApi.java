package by.dytni.innoviseproject.api;


import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import by.dytni.innoviseproject.dto.card.Card;
import by.dytni.innoviseproject.dto.card.CardMaker;
import by.dytni.innoviseproject.dto.card.CardUpdater;

public interface CardControllerApi {


    ResponseEntity<Card> createCard(CardMaker cardMaker);
    ResponseEntity<Card> updateCard(CardUpdater cardUpdater, Long id);
    ResponseEntity<Card> deleteCard(Long id);
    ResponseEntity<Page<Card>> getAllCards(String name, String lastName, int page, int size);
    ResponseEntity<Card> getCardById(Long id);
    ResponseEntity<Card> changeStatus(Long id);
}
