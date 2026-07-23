package by.dytni.innoviseproject.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import by.dytni.innoviseproject.dto.card.Card;
import by.dytni.innoviseproject.dto.card.CardFilter;
import by.dytni.innoviseproject.dto.card.CardMaker;
import by.dytni.innoviseproject.dto.card.CardUpdater;

public interface CardService {

    Card createCard(CardMaker cardMaker);

    Card updateCard(CardUpdater cardUpdater, Long cardId);

    Card deleteCard(Long cardId);

    Page<Card> getAllCards(CardFilter filter);

    Page<Card> getAllCardsByUserId(Pageable pageable, Long userId);

    Card getCardById(Long id);

    Card changeStatus(Long cardId);

}
