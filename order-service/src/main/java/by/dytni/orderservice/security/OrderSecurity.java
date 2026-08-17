package by.dytni.orderservice.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import by.dytni.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;


@Component("orderSecurity")
@RequiredArgsConstructor
public class OrderSecurity {

    private final OrderService orderService;

    public boolean isOwner(Long orderId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication.getPrincipal() instanceof Long userId))
            return false;


        return orderService.existsByIdAndUserId(orderId, userId);
    }

}
