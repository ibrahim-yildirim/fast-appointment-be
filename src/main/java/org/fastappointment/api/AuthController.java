package org.fastappointment.api;

import jakarta.validation.Valid;
import org.fastappointment.api.dto.AuthResponse;
import org.fastappointment.api.dto.LoginRequest;
import org.fastappointment.api.dto.RefreshTokenRequest;
import org.fastappointment.api.dto.RegisterCustomerRequest;
import org.fastappointment.api.dto.RegisterRequest;
import org.fastappointment.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/auth/register")
    public ResponseEntity<AuthResponse> registerCustomer(@Valid @RequestBody RegisterCustomerRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.registerCustomer(request));
    }

    @PostMapping("/business/auth/register")
    public ResponseEntity<AuthResponse> registerBusinessOwner(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.registerBusinessOwner(request));
    }

    @PostMapping("/auth/login")
    public ResponseEntity<AuthResponse> loginCustomer(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.loginCustomer(request));
    }

    @PostMapping("/business/auth/login")
    public ResponseEntity<AuthResponse> loginBusinessOwner(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.loginBusinessOwner(request));
    }

    @PostMapping("/auth/refresh")
    public ResponseEntity<AuthResponse> refresh(@Valid @RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(authService.refresh(request));
    }

    @PostMapping("/auth/logout")
    public ResponseEntity<Void> logout(@Valid @RequestBody RefreshTokenRequest request) {
        authService.logout(request);
        return ResponseEntity.noContent().build();
    }
}
