package by.dytni.innoviseproject.service;

import static by.dytni.innoviseproject.InnoviseProjectConstantsTest.CARD_ACTIVE;
import static by.dytni.innoviseproject.InnoviseProjectConstantsTest.CARD_ANOTHER_ID;
import static by.dytni.innoviseproject.InnoviseProjectConstantsTest.CARD_ANOTHER_NUMBER;
import static by.dytni.innoviseproject.InnoviseProjectConstantsTest.CARD_DEACTIVE;
import static by.dytni.innoviseproject.InnoviseProjectConstantsTest.CARD_EXPIRY_DATE;
import static by.dytni.innoviseproject.InnoviseProjectConstantsTest.CARD_HOLDER;
import static by.dytni.innoviseproject.InnoviseProjectConstantsTest.CARD_ID;
import static by.dytni.innoviseproject.InnoviseProjectConstantsTest.CARD_NUMBER;
import static by.dytni.innoviseproject.InnoviseProjectConstantsTest.USER_ACTIVE;
import static by.dytni.innoviseproject.InnoviseProjectConstantsTest.USER_BIRTH_DATE;
import static by.dytni.innoviseproject.InnoviseProjectConstantsTest.USER_EMAIL;
import static by.dytni.innoviseproject.InnoviseProjectConstantsTest.USER_FIRST_NAME;
import static by.dytni.innoviseproject.InnoviseProjectConstantsTest.USER_ID;
import static by.dytni.innoviseproject.InnoviseProjectConstantsTest.USER_LAST_NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import by.dytni.innoviseproject.dto.card.Card;
import by.dytni.innoviseproject.dto.card.CardFilter;
import by.dytni.innoviseproject.dto.card.CardMaker;
import by.dytni.innoviseproject.dto.card.CardUpdater;
import by.dytni.innoviseproject.exceptions.CardsLimitException;
import by.dytni.innoviseproject.mapper.CardCriteriaMapper;
import by.dytni.innoviseproject.mapper.CardMapper;
import by.dytni.innoviseproject.repository.CardRepository;
import by.dytni.innoviseproject.repository.UserRepository;
import by.dytni.innoviseproject.repository.criteria.CardCriteria;
import by.dytni.innoviseproject.repository.entity.CardEntity;
import by.dytni.innoviseproject.repository.entity.UserEntity;
import by.dytni.innoviseproject.repository.specification.CardSpecifications;
import by.dytni.innoviseproject.service.implementation.CardServiceImpl;

@ExtendWith(MockitoExtension.class)
public class CardServiceTest {
    @Mock
    private CardRepository cardRepository;
    @Mock
    private CardMapper cardMapper;
    @Mock
    private CardCriteriaMapper criteriaMapper;
    @Mock
    private CardSpecifications cardSpecifications;
    @Mock
    Specification<CardEntity> specification;
    @Mock
    UserRepository userRepository;

    @InjectMocks
    CardServiceImpl service;

    private CardEntity cardEntity;
    private Card card;
    private UserEntity userEntity;
    private CardFilter cardFilter;
    private Pageable pageable;

    @BeforeEach
    void setUp() {

        cardEntity = CardEntity.builder()
                .id(CARD_ID)
                .user(userEntity)
                .cardNumber(CARD_NUMBER)
                .expiryDate(CARD_EXPIRY_DATE)
                .holderName(CARD_HOLDER)
                .activeStatus(CARD_ACTIVE)
                .build();
        List<CardEntity> cardEntities = List.of(cardEntity);

        userEntity = UserEntity.builder()
                .id(USER_ID)
                .activeStatus(USER_ACTIVE)
                .email(USER_EMAIL)
                .birthDate(USER_BIRTH_DATE)
                .firstName(USER_FIRST_NAME)
                .lastName(USER_LAST_NAME)
                .cards(cardEntities)
                .build();
        card = Card.builder()
                .id(CARD_ID)
                .userId(USER_ID)
                .cardNumber(CARD_NUMBER)
                .expiryDate(CARD_EXPIRY_DATE)
                .holderName(CARD_HOLDER)
                .activeStatus(CARD_ACTIVE)
                .build();

    }


    @AfterEach
    void complete() {
        verifyNoMoreInteractions(cardRepository, cardMapper, criteriaMapper, specification, cardSpecifications);
    }

    @Test
    void get_all_cards() {
        cardFilter = CardFilter.builder()
                .page(0)
                .size(10)
                .build();
        pageable = PageRequest.of(cardFilter.getPage(), cardFilter.getSize());
        Page<CardEntity> cardEntityPage = new PageImpl<>(List.of(cardEntity, cardEntity, cardEntity), pageable, 3);
        cardFilter = CardFilter.builder()
                .page(0)
                .size(10)
                .build();
        pageable = PageRequest.of(cardFilter.getPage(), cardFilter.getSize());
        CardCriteria cardCriteria = CardCriteria.builder()
                .pageable(pageable)
                .build();

        when(criteriaMapper.dtoToCriteria(cardFilter)).thenReturn(cardCriteria);
        when(cardSpecifications.getSpecification(cardCriteria)).thenReturn(specification);
        when(cardRepository.findAll(specification, pageable)).thenReturn(cardEntityPage);
        when(cardMapper.entityToDto(cardEntity)).thenReturn(card);

        Page<Card> result = service.getAllCards(cardFilter);
        assertThat(result).hasSize(3);
        assertThat(result.getContent().getFirst()).isEqualTo(card);

        verify(criteriaMapper, times(1)).dtoToCriteria(cardFilter);
        verify(cardSpecifications, times(1)).getSpecification(cardCriteria);
        verify(cardRepository, times(1)).findAll(specification, pageable);
        verify(cardMapper, times(3)).entityToDto(cardEntity);

    }

    @Test
    void get_card_by_id() {
        when(cardRepository.findById(CARD_ID)).thenReturn(Optional.of(cardEntity));
        when(cardMapper.entityToDto(cardEntity)).thenReturn(card);

        Card result = service.getCardById(CARD_ID);
        assertThat(result).isEqualTo(card);

        verify(cardRepository, times(1)).findById(CARD_ID);
        verify(cardMapper, times(1)).entityToDto(cardEntity);

    }

    @Test
    void create_card() {
        CardMaker cardMaker = CardMaker.builder()
                .userId(USER_ID)
                .cardNumber(CARD_NUMBER)
                .build();

        when(userRepository.findByIdWithCards(USER_ID)).thenReturn(Optional.of(userEntity));
        when(cardMapper.dtoToEntity(cardMaker, userEntity)).thenReturn(cardEntity);
        when(cardRepository.save(cardEntity)).thenReturn(cardEntity);
        when(cardMapper.entityToDto(cardEntity)).thenReturn(card);

        Card result = service.createCard(cardMaker);
        assertThat(result).isEqualTo(card);

        verify(userRepository, times(1)).findByIdWithCards(USER_ID);
        verify(cardMapper, times(1)).dtoToEntity(cardMaker, userEntity);
        verify(cardRepository, times(1)).save(cardEntity);
        verify(cardMapper, times(1)).entityToDto(cardEntity);
    }

    @Test
    void create_more_then_five_cards() {
        List<CardEntity> fiveCards = List.of(cardEntity, cardEntity, cardEntity, cardEntity, cardEntity);
        UserEntity userWithFiveCards = UserEntity.builder()
                .id(USER_ID)
                .cards(fiveCards)
                .build();
        CardMaker cardMaker = CardMaker.builder()
                .userId(USER_ID)
                .build();

        when(userRepository.findByIdWithCards(USER_ID)).thenReturn(Optional.of(userWithFiveCards));
        assertThrows(CardsLimitException.class, () -> service.createCard(cardMaker));
        verify(userRepository, times(1)).findByIdWithCards(USER_ID);
    }

    @Test
    void update_card() {
        CardEntity changedCardEntity = CardEntity.builder()
                .id(CARD_ANOTHER_ID)
                .cardNumber(CARD_ANOTHER_NUMBER)
                .build();
        Card changedCard = Card.builder()
                .id(CARD_ANOTHER_ID)
                .cardNumber(CARD_ANOTHER_NUMBER)
                .build();
        CardUpdater cardUpdater = CardUpdater.builder()
                .cardNumber(CARD_ANOTHER_NUMBER)
                .build();

        when(cardRepository.findById(CARD_ANOTHER_ID)).thenReturn(Optional.of(cardEntity));
        when(cardMapper.updateEntity(cardEntity, cardUpdater)).thenReturn(changedCardEntity);
        when(cardRepository.save(changedCardEntity)).thenReturn(changedCardEntity);
        when(cardMapper.entityToDto(cardEntity)).thenReturn(changedCard);

        Card result = service.updateCard(cardUpdater, CARD_ANOTHER_ID);
        assertThat(result).isEqualTo(changedCard);

        verify(cardRepository, times(1)).findById(CARD_ANOTHER_ID);
        verify(cardMapper, times(1)).updateEntity(cardEntity, cardUpdater);
        verify(cardRepository, times(1)).save(changedCardEntity);
        verify(cardMapper, times(1)).entityToDto(cardEntity);

    }

    @Test
    void delete_card() {

        when(cardRepository.findById(CARD_ID)).thenReturn(Optional.of(cardEntity));
        when(cardMapper.entityToDto(cardEntity)).thenReturn(card);

        service.deleteCard(CARD_ID);

        verify(cardRepository, times(1)).findById(CARD_ID);
        verify(cardRepository, times(1)).delete(cardEntity);
    }

    @Test
    void change_card_status() {
        Card changedCard = Card.builder()
                .id(CARD_ID)
                .activeStatus(CARD_DEACTIVE)
                .build();

        when(cardRepository.findById(CARD_ID)).thenReturn(Optional.of(cardEntity));
        when(cardMapper.entityToDto(cardEntity)).thenReturn(changedCard);

        Card result = service.changeStatus(CARD_ID);
        assertThat(result).isEqualTo(changedCard);

        verify(cardRepository, times(1)).findById(CARD_ID);
        verify(cardRepository, times(1)).changeCardStatus(CARD_ID, CARD_DEACTIVE);
        verify(cardMapper, times(1)).entityToDto(cardEntity);
    }

    @Test
    void get_cards_by_user_id() {
        cardFilter = CardFilter.builder()
                .page(0)
                .size(10)
                .build();
        pageable = PageRequest.of(cardFilter.getPage(), cardFilter.getSize());

        Page<CardEntity> cardEntityPage = new PageImpl<>(List.of(cardEntity, cardEntity, cardEntity), pageable, 3);

        when(cardRepository.findAllByUserId(USER_ID, pageable)).thenReturn(cardEntityPage);
        when(cardMapper.entityToDto(cardEntity)).thenReturn(card);

        Page<Card> result = service.getAllCardsByUserId(pageable, USER_ID);

        assertThat(result).hasSize(3);
        assertThat(result.getContent().getFirst()).isEqualTo(card);

        verify(cardRepository, times(1)).findAllByUserId(USER_ID, pageable);
        verify(cardMapper, times(3)).entityToDto(cardEntity);
    }

}
