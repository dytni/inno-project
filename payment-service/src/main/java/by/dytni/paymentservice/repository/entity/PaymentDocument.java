package by.dytni.paymentservice.repository.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Document(collection = "payments")
@Builder
@Getter
@Setter
public class PaymentDocument {

    @Id
    private String id;

    @Field("order_id")
    private Long orderId;

    @Field("user_id")
    private Long userId;

    private PaymentStatus status;

    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

    @Field(name = "payment_amount", targetType = FieldType.DECIMAL128)
    private BigDecimal paymentAmount;
}