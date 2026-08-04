package by.dytni.auth.service;

import by.dytni.auth.repository.model.Role;

public interface JwtService {

    String generateAccessToken(Long userId, Role role);

    String generateRefreshToken(Long userId);

    boolean validate(String token);

    Long getUserId(String token);

}