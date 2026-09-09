package by.dytni.paymentservice.controller;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import by.dytni.paymentservice.dto.Payment;
import by.dytni.paymentservice.dto.PaymentFilter;
import by.dytni.paymentservice.dto.PaymentMaker;
import by.dytni.paymentservice.dto.PaymentSum;
import by.dytni.paymentservice.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;



    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Payment createPayment(@RequestBody PaymentMaker paymentMaker) {
        return paymentService.createPayment(paymentMaker);
    }

    @GetMapping("/sum/user/{userId}")
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal")
    public PaymentSum getSumForUser(
            @PathVariable Long userId,
            @RequestParam LocalDateTime from,
            @RequestParam LocalDateTime to) {
        return paymentService.getSumForUser(userId, from, to);
    }

    @GetMapping("/sum/all")
    @PreAuthorize("hasRole('ADMIN')")
    public PaymentSum getSumForAllUsers(
            @RequestParam LocalDateTime from,
            @RequestParam LocalDateTime to) {
        return paymentService.getSumForAllUsers(from, to);
    }

    @GetMapping()
    @PreAuthorize("hasRole('ADMIN') or #filter.userId == authentication.principal")
    public Page<Payment> searchPayments(@Valid @ModelAttribute PaymentFilter filter) {
        return paymentService.searchPayments(filter);
    }
}
