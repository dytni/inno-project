package by.dytni.paymentservice.repository;

import java.time.LocalDateTime;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import by.dytni.paymentservice.dto.PaymentSum;
import by.dytni.paymentservice.repository.entity.PaymentDocument;

@Repository
public interface PaymentRepository extends MongoRepository<PaymentDocument, String>, PaymentSearchRepository{

    @Aggregation(pipeline = {
            "{ $match: { 'user_id': ?0, 'timestamp': { $gte: ?1, $lte: ?2 } } }",
            "{ $group: { '_id': null, 'sum': { $sum: '$payment_amount' } } }"
    })
    PaymentSum getSumByUserIdAndDateRange(Long userId, LocalDateTime from, LocalDateTime to);
    @Aggregation(pipeline = {
            "{ $match: { 'timestamp': { $gte: ?0, $lte: ?1 } } }",
            "{ $group: { '_id': null, 'sum': { $sum: '$payment_amount' } } }"
    })
    PaymentSum getSumByDateRange(LocalDateTime from, LocalDateTime to);

}
