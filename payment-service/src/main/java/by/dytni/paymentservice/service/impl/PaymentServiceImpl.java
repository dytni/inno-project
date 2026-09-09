package by.dytni.paymentservice.service.impl;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import by.dytni.commonevents.dto.PaymentCreateEvent;
import by.dytni.paymentservice.client.RandomNumberClient;
import by.dytni.paymentservice.dto.Payment;
import by.dytni.paymentservice.dto.PaymentFilter;
import by.dytni.paymentservice.dto.PaymentMaker;
import by.dytni.paymentservice.dto.PaymentSum;
import by.dytni.paymentservice.dto.RandomNumber;
import by.dytni.paymentservice.kafka.PaymentCreateProducer;
import by.dytni.paymentservice.mapper.PaymentCriteriaMapper;
import by.dytni.paymentservice.mapper.PaymentMapper;
import by.dytni.paymentservice.repository.PaymentRepository;
import by.dytni.paymentservice.repository.criteria.PaymentCriteria;
import by.dytni.paymentservice.repository.entity.PaymentDocument;
import by.dytni.paymentservice.repository.entity.PaymentStatus;
import by.dytni.paymentservice.service.PaymentService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {


    private final PaymentCreateProducer createProducer;
    private final RandomNumberClient randomNumberClient;
    private final PaymentMapper paymentMapper;
    private final PaymentCriteriaMapper paymentCriteriaMapper;
    private final PaymentRepository paymentRepository;

    @Override
    public PaymentSum getSumForUser(Long userId, LocalDateTime from, LocalDateTime to) {
        return paymentRepository.getSumByUserIdAndDateRange(userId, from, to);
    }

    @Override
    public Payment createPayment(PaymentMaker paymentMaker) {

        PaymentDocument payment = paymentMapper.dtoToEntity(paymentMaker);
        RandomNumber randomNumber = randomNumberClient.generateRandomNumber();
        if(randomNumber.getRandomNumber() % 2 == 0){
            payment.setStatus(PaymentStatus.SUCCESS);
        }
        else {
            payment.setStatus(PaymentStatus.FAILED);
        }

        PaymentDocument savedPayment = paymentRepository.save(payment);

        PaymentCreateEvent event = new PaymentCreateEvent(
                savedPayment.getOrderId(),
                savedPayment.getStatus().equals(PaymentStatus.SUCCESS)
        );
        createProducer.send(event);

        return paymentMapper.entityToDto(savedPayment);
    }



    @Override
    public Page<Payment> searchPayments(PaymentFilter paymentFilter) {
        PaymentCriteria criteria = paymentCriteriaMapper.filterToCriteria(paymentFilter);
        Page<PaymentDocument> payments = paymentRepository.searchPayments(criteria);
        return payments.map(paymentMapper::entityToDto);
    }


    @Override
    public PaymentSum getSumForAllUsers(LocalDateTime from, LocalDateTime to) {
        return paymentRepository.getSumByDateRange(from, to);
    }
}
