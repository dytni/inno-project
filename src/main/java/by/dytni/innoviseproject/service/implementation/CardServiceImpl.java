package by.dytni.innoviseproject.service.implementation;

import static by.dytni.innoviseproject.InnoviseProjectConstants.CARDS_LIMIT_ERROR;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import by.dytni.innoviseproject.dto.card.Card;
import by.dytni.innoviseproject.dto.card.CardFilter;
import by.dytni.innoviseproject.dto.card.CardMaker;
import by.dytni.innoviseproject.dto.card.CardUpdater;
import by.dytni.innoviseproject.exceptions.CardNotFoundException;
import by.dytni.innoviseproject.exceptions.CardsLimitException;
import by.dytni.innoviseproject.exceptions.UserNotFoundException;
import by.dytni.innoviseproject.mapper.CardCriteriaMapper;
import by.dytni.innoviseproject.mapper.CardMapper;
import by.dytni.innoviseproject.repository.CardRepository;
import by.dytni.innoviseproject.repository.UserRepository;
import by.dytni.innoviseproject.repository.criteria.CardCriteria;
import by.dytni.innoviseproject.repository.entity.CardEntity;
import by.dytni.innoviseproject.repository.entity.UserEntity;
import by.dytni.innoviseproject.repository.specification.CardSpecifications;
import by.dytni.innoviseproject.service.CardService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;
    private final CardMapper cardMapper;
    private final CardCriteriaMapper cardCriteriaMapper;
    private final UserRepository userRepository;
    private final CardSpecifications cardSpecifications;

    @Override
    @Transactional
    @CachePut(value = "cards", key = "#result.id")
    @CacheEvict(value = "cardsByUserId", key = "#result.userId")
    public Card createCard(CardMaker cardMaker) {
        log.info("Create card: {}", cardMaker);
        Long userId = cardMaker.getUserId();
        UserEntity user = userRepository.findByIdWithCards(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        if(user.getCards().size() >= 5){
            throw new CardsLimitException(CARDS_LIMIT_ERROR);
        }
        CardEntity cardEntity = cardMapper.dtoToEntity(cardMaker, user);
        cardEntity.setHolderName(user.getFirstName() + " " + user.getLastName());
        cardEntity.setUser(user);
        cardRepository.save(cardEntity);
        return cardMapper.entityToDto(cardEntity);
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "cards", key = "#cardId"),
            @CacheEvict(value = "cardsByUserId", key = "#result.userId")
    })
    public Card updateCard(CardUpdater cardUpdater, Long cardId) {
        log.info("Update card: {}", cardUpdater);
        CardEntity cardEntity = cardRepository.findById(cardId)
                .orElseThrow(() -> new CardNotFoundException(cardId));
        cardRepository.save(cardMapper.updateEntity(cardEntity, cardUpdater));
        return cardMapper.entityToDto(cardEntity);
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "cards", key = "#cardId"),
            @CacheEvict(value = "cardsByUserId", key = "#result.userId")
    })
    public Card deleteCard(Long cardId) {
        log.info("Delete card: {}", cardId);
        CardEntity cardEntity = cardRepository.findById(cardId)
                .orElseThrow(() -> new CardNotFoundException(cardId));
        cardRepository.delete(cardEntity);
        return cardMapper.entityToDto(cardEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Card> getAllCards(CardFilter filter) {
        log.info("Get all cards: {}", filter);
        CardCriteria criteria = cardCriteriaMapper.dtoToCriteria(filter);
        Specification<CardEntity> spec = cardSpecifications.getSpecification(criteria);
        return cardRepository.findAll(spec, criteria.getPageable()).map(cardMapper::entityToDto);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "cardsByUserId", key = "#userId")
    public Page<Card> getAllCardsByUserId(Pageable pageable, Long userId) {
        log.info("Get all cards by user: {}", userId);
        return cardRepository.findAllByUserId(userId, pageable).map(cardMapper::entityToDto);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "cards", key = "#cardId")
    public Card getCardById(Long cardId) {
        log.info("Get card by id: {}", cardId);
        return cardRepository.findById(cardId).map(cardMapper::entityToDto)
                .orElseThrow(() -> new CardNotFoundException(cardId));
    }

    @Override
    @Transactional
    @CacheEvict(value = "cardsByUserId", key = "#result.userId")
    public Card changeStatus(Long cardId) {
        log.info("Change status of card: {}", cardId);
        CardEntity cardEntity = cardRepository.findById(cardId)
                .orElseThrow(() -> new CardNotFoundException(cardId));
        cardRepository.changeCardStatus(cardId, !cardEntity.getActiveStatus());
        return cardMapper.entityToDto(cardEntity);
    }
}
