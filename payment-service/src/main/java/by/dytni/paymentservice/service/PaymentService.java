package by.dytni.paymentservice.service;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;

import by.dytni.paymentservice.dto.Payment;
import by.dytni.paymentservice.dto.PaymentFilter;
import by.dytni.paymentservice.dto.PaymentMaker;
import by.dytni.paymentservice.dto.PaymentSum;

public interface PaymentService {
    PaymentSum getSumForUser(Long userId, LocalDateTime from, LocalDateTime to);

    Payment createPayment(PaymentMaker paymentMaker);

    Page<Payment> searchPayments(PaymentFilter paymentFilter);


    PaymentSum getSumForAllUsers(LocalDateTime from, LocalDateTime to);
}
