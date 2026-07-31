package by.dytni.auth.service;

import by.dytni.auth.dto.auth.AuthRequest;
import by.dytni.auth.dto.JwtResponse;
import by.dytni.auth.dto.register.RegisterRequest;


public interface AuthenticationService {

    JwtResponse login(AuthRequest request);

    JwtResponse register(RegisterRequest request);

    JwtResponse refresh(String token);

    void validate(String token);

    void changeStatus(String login, Boolean status);

    void makeAdmin(String login);

    void changeLogin(String login, String newLogin);

}
