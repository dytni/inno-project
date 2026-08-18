package by.dytni.orderservice.controller;

import static by.dytni.orderservice.OrderServiceConstants.DEFAULT_PAGE;
import static by.dytni.orderservice.OrderServiceConstants.DEFAULT_PAGE_SIZE;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import by.dytni.orderservice.dto.order.Order;
import by.dytni.orderservice.dto.order.OrderFilter;
import by.dytni.orderservice.dto.order.OrderMaker;
import by.dytni.orderservice.dto.order.OrderUpdater;
import by.dytni.orderservice.service.OrderService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/order")
@AllArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PreAuthorize("hasRole('ADMIN') or #orderMaker.userId == authentication.principal")
    @PostMapping
    public ResponseEntity<Order> createOrder(@Valid @RequestBody OrderMaker orderMaker) {
        return ResponseEntity.status(CREATED).body(orderService.createOrder(orderMaker));
    }

    @PreAuthorize("hasRole('ADMIN') or @orderSecurity.isOwner(#id)")
    @PutMapping("/{id}")
    public ResponseEntity<Order> updateOrder(@Valid @RequestBody OrderUpdater orderUpdater, @PathVariable Long id) {
        return ResponseEntity.status(OK).body(orderService.updateOrder(orderUpdater, id));
    }

    @PreAuthorize("hasRole('ADMIN') or @orderSecurity.isOwner(#id)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Order> deleteOrder(@PathVariable Long id) {
        return ResponseEntity.status(OK).body(orderService.deleteOrder(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<Page<Order>> getAllOrders(@Valid @ModelAttribute OrderFilter orderFilter){
        return ResponseEntity.status(OK).body(orderService.getAllOrders(orderFilter));
    }

    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal")
    @GetMapping("/user")
    public ResponseEntity<Page<Order>> getOrdersByUserId(
            @RequestParam Long userId,
            @RequestParam(defaultValue = DEFAULT_PAGE) int page,
            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int size){
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.status(OK).body(orderService.getOrdersByUserId(userId, pageable));
    }

    @PreAuthorize("hasRole('ADMIN') or @orderSecurity.isOwner(#id)")
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        return ResponseEntity.status(OK).body(orderService.getOrderById(id));
    }

}
