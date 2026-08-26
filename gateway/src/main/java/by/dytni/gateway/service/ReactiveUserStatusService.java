package by.dytni.gateway.service;

import reactor.core.publisher.Mono;

public interface ReactiveUserStatusService {

    Mono<Boolean> isActive(Long userId);
}
