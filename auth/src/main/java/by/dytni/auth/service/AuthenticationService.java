package by.dytni.auth.service;

import by.dytni.auth.dto.AuthRequest;
import by.dytni.auth.dto.JwtResponse;
import by.dytni.auth.dto.RegisterRequest;

public interface AuthenticationService {

    JwtResponse login(AuthRequest request);

    JwtResponse register(RegisterRequest request);

    JwtResponse refresh(String token);

    void validate(String token);

    void changeStatus(String login, Boolean status);
}
