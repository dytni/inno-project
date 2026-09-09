package by.dytni.commonevents;

import lombok.experimental.UtilityClass;

@UtilityClass
public class CommonsKafkaConstants {


    public static final String KAFKA_USER_STATUS_TOPIC = "user-status";
    public static final String KAFKA_USER_CREATED_TOPIC = "user-created";
    public static final String KAFKA_USER_ROLLBACK_TOPIC = "user-rollback";
    public static final String KAFKA_USER_LOGIN_TOPIC = "user-login";

    public static final String KAFKA_PAYMENT_CREATED_TOPIC = "payment-created";
}
