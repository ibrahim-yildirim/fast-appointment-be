package org.fastappointment.service;

import org.fastappointment.api.dto.AuthResponse;
import org.fastappointment.api.dto.LoginRequest;
import org.fastappointment.api.dto.RefreshTokenRequest;
import org.fastappointment.api.dto.RegisterCustomerRequest;
import org.fastappointment.api.dto.RegisterRequest;

public interface AuthService {

    AuthResponse registerCustomer(RegisterCustomerRequest request);

    AuthResponse registerBusinessOwner(RegisterRequest request);

    AuthResponse loginCustomer(LoginRequest request);

    AuthResponse loginBusinessOwner(LoginRequest request);

    AuthResponse refresh(RefreshTokenRequest request);

    void logout(RefreshTokenRequest request);
}
