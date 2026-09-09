package by.dytni.paymentservice.repository;

import org.springframework.data.domain.Page;

import by.dytni.paymentservice.repository.criteria.PaymentCriteria;
import by.dytni.paymentservice.repository.entity.PaymentDocument;

public interface PaymentSearchRepository {
    Page<PaymentDocument> searchPayments(PaymentCriteria criteria);
}
