package by.dytni.paymentservice.repository.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;


import by.dytni.paymentservice.repository.PaymentSearchRepository;
import by.dytni.paymentservice.repository.criteria.PaymentCriteria;
import by.dytni.paymentservice.repository.entity.PaymentDocument;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PaymentSearchRepositoryImpl implements PaymentSearchRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public Page<PaymentDocument> searchPayments(PaymentCriteria criteria) {

        Query query = new Query();
        if (criteria.getUserId() != null) {
            query.addCriteria(Criteria.where("user_id").is(criteria.getUserId()));
        }
        if (criteria.getOrderId() != null) {
            query.addCriteria(Criteria.where("order_id").is(criteria.getOrderId()));
        }
        if (criteria.getStatus() != null) {
            query.addCriteria(Criteria.where("status").is(criteria.getStatus()));
        }

        long total = mongoTemplate.count(query, PaymentDocument.class);
        query.with(criteria.getPageable());
        List<PaymentDocument> payments = mongoTemplate.find(query, PaymentDocument.class);

        return new PageImpl<>(payments, criteria.getPageable(), total);
    }
}
