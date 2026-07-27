package by.dytni.userservice.controller;

import static by.dytni.userservice.UserServiceConstants.DEFAULT_PAGE;
import static by.dytni.userservice.UserServiceConstants.DEFAULT_PAGE_SIZE;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import by.dytni.userservice.api.CardControllerApi;
import by.dytni.userservice.dto.card.Card;
import by.dytni.userservice.dto.card.CardFilter;
import by.dytni.userservice.dto.card.CardMaker;
import by.dytni.userservice.dto.card.CardUpdater;
import by.dytni.userservice.service.CardService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/card")
@AllArgsConstructor
public class CardController implements CardControllerApi {

    private final CardService cardService;




    @PreAuthorize("hasRole('ADMIN') or #paymentCardDTO.userId == authentication.principal")
    @PostMapping
    public ResponseEntity<Card> createCard(@Valid @RequestBody CardMaker cardMaker) {
        return ResponseEntity.status(CREATED).body(cardService.createCard(cardMaker));
    }

    @PreAuthorize("hasRole('ADMIN') or @cardSecurity.isOwner(#id)")
    @PutMapping("/{id}")
    public ResponseEntity<Card> updateCard(@RequestBody @Valid CardUpdater cardUpdater,@PathVariable  Long id) {
        return ResponseEntity.status(OK).body(cardService.updateCard(cardUpdater, id));
    }

    @PreAuthorize("hasRole('ADMIN') or @cardSecurity.isOwner(#id)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Card> deleteCard(@PathVariable Long id) {
        return ResponseEntity.status(OK).body(cardService.deleteCard(id));
    }


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<Page<Card>> getAllCards(
                                                  @RequestParam(required = false) String name,
                                                  @RequestParam(required = false) String lastName,
                                                  @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                                  @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int size){
        return ResponseEntity.status(OK).body(cardService.getAllCards(CardFilter.builder()
                                                      .userFirstName(name)
                                                      .userLastName(lastName)
                                                      .page(page)
                                                      .size(size)
                                                      .build()));
    }



    @PreAuthorize("hasRole('ADMIN') or @cardSecurity.isOwner(#id)")
    @GetMapping("/{id}")
    public ResponseEntity<Card> getCardById(@PathVariable Long id) {
        return ResponseEntity.status(OK).body(cardService.getCardById(id));
    }

    @PreAuthorize("hasRole('ADMIN') or @cardSecurity.isOwner(#id)")
    @PutMapping("/active/{id}")
    public ResponseEntity<Card> changeStatus(@PathVariable Long id) {
        return ResponseEntity.status(OK).body(cardService.changeStatus(id));
    }


    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal")
    @GetMapping("/{userId}/user")
    public ResponseEntity<Page<Card>> getAllByUserId(
            @PathVariable Long userId,
            @RequestParam(defaultValue = DEFAULT_PAGE) int page,
            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.status(OK).body(cardService.getAllCardsByUserId(pageable, userId));
    }

}
