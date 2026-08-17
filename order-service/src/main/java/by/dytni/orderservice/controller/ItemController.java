package by.dytni.orderservice.controller;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import by.dytni.orderservice.dto.item.Item;
import by.dytni.orderservice.dto.item.ItemMaker;
import by.dytni.orderservice.dto.item.ItemUpdater;
import by.dytni.orderservice.service.ItemService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/order/item")
@AllArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Item> createItem(@Valid @RequestBody ItemMaker itemMaker) {
        return ResponseEntity.status(CREATED).body(itemService.createItem(itemMaker));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Item> updateItem(@Valid @RequestBody ItemUpdater itemUpdater, @PathVariable Long id) {
        return ResponseEntity.status(CREATED).body(itemService.updateItem(itemUpdater, id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Item> deleteOrder(@PathVariable Long id) {
        return ResponseEntity.status(OK).body(itemService.deleteItem(id));
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<Item> getOrderById(@PathVariable Long id) {
        return ResponseEntity.status(OK).body(itemService.getItemById(id));
    }

}
