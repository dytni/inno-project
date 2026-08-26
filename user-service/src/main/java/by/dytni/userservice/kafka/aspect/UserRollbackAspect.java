package by.dytni.userservice.kafka.aspect;

import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import by.dytni.commonevents.dto.UserCreatedEvent;
import by.dytni.commonevents.dto.UserRollbackEvent;
import by.dytni.userservice.kafka.producer.UserRollbackProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class UserRollbackAspect {
    private final UserRollbackProducer userRollbackProducer;

    @AfterThrowing(
            pointcut = "execution(* by.dytni.userservice.service.UserService.createUserFromAuth(..)) && args(userCreatedEvent)",
            throwing = "exception"
    )
    public void publishRollbackEventOnError(UserCreatedEvent userCreatedEvent, Exception exception) {
        log.error("Exception in createUser for authUserId: {}. Publishing Rollback Event...",
                  userCreatedEvent.authUserId(), exception);

        UserRollbackEvent rollbackEvent = new UserRollbackEvent(userCreatedEvent.authUserId(), exception.getMessage());
        userRollbackProducer.send(rollbackEvent);
    }
}
