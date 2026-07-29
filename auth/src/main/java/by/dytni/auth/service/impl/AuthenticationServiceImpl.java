package by.dytni.auth.service.impl;


import static by.dytni.auth.AuthConstant.INVALID_LOGIN_ERROR;
import static by.dytni.auth.AuthConstant.INVALID_PASSWORD_ERROR;
import static by.dytni.auth.AuthConstant.INVALID_REFRESH_TOKEN_ERROR;
import static by.dytni.auth.AuthConstant.INVALID_TOKEN_ERROR;
import static by.dytni.auth.AuthConstant.LOGIN_ALREADY_EXIST_ERROR;

import by.dytni.auth.dto.AuthRequest;
import by.dytni.auth.dto.JwtResponse;
import by.dytni.auth.dto.RegisterRequest;
import by.dytni.auth.exception.UserAlreadyExist;
import by.dytni.auth.exception.UserNotFoundException;
import by.dytni.auth.mapper.UserMapper;
import by.dytni.auth.repository.UserRepository;
import by.dytni.auth.repository.model.UserEntity;
import by.dytni.auth.service.AuthenticationService;
import by.dytni.auth.service.JwtService;
import io.jsonwebtoken.JwtException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;


    @Override
    @Transactional(readOnly = true)
    public JwtResponse login(AuthRequest request) {
        log.info("Login request: {}", request);
        UserEntity user = userRepository.findByLogin(request.getLogin())
                .orElseThrow(() -> new BadCredentialsException(INVALID_LOGIN_ERROR));

        if(!passwordEncoder.matches(request.getPassword(), user.getPasswordHash()))
            throw new BadCredentialsException(INVALID_PASSWORD_ERROR);

        return JwtResponse.builder()
                .accessToken(jwtService.generateAccessToken(user.getId(), user.getRole()))
                .refreshToken(jwtService.generateRefreshToken(user.getId()))
                .build();
    }

    @Override
    @Transactional
    public JwtResponse register(RegisterRequest request) {
        log.info("Register request: {}", request);
        if(userRepository.existsByLogin(request.getLogin()))
            throw new UserAlreadyExist(LOGIN_ALREADY_EXIST_ERROR);

        UserEntity user = userMapper.dtoToEntity(request);
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        UserEntity savedUser = userRepository.save(user);

        return JwtResponse.builder()
                .accessToken(jwtService.generateAccessToken(savedUser.getId(), savedUser.getRole()))
                .refreshToken(jwtService.generateRefreshToken(savedUser.getId()))
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public JwtResponse refresh(String token) {
        log.info("Refresh token: {}", token);
        if (!jwtService.validate(token))
            throw new JwtException(INVALID_REFRESH_TOKEN_ERROR);

        Long userId = jwtService.getUserId(token);
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        return JwtResponse.builder()
                .accessToken(jwtService.generateAccessToken(userId, user.getRole()))
                .refreshToken(jwtService.generateRefreshToken(userId))
                .build();

    }

    @Override
    public void validate(String token) {
        log.info("Validate token: {}", token);
        if (!jwtService.validate(token)) throw new JwtException(INVALID_TOKEN_ERROR);
    }
}