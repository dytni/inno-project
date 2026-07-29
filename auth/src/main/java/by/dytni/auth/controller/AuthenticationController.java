package by.dytni.auth.controller;

import static org.springframework.http.HttpStatus.CREATED;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import by.dytni.auth.dto.AuthRequest;
import by.dytni.auth.dto.JwtResponse;
import by.dytni.auth.dto.RegisterRequest;
import by.dytni.auth.service.AuthenticationService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("api/auth")
@AllArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/register")
    public ResponseEntity<JwtResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.status(CREATED).body(authenticationService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login (@RequestBody AuthRequest request) {
        return ResponseEntity.ok(authenticationService.login(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> refresh(@RequestParam String token) {
        return ResponseEntity.ok(authenticationService.refresh(token));
    }

    @GetMapping("/validate")
    public ResponseEntity<Void> validate(@RequestParam String token) {
        authenticationService.validate(token);
        return ResponseEntity.ok().build();
    }
}
