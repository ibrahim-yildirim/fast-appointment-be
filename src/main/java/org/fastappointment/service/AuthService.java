package org.fastappointment.service;

import org.fastappointment.api.dto.AuthResponse;
import org.fastappointment.api.dto.LoginRequest;
import org.fastappointment.api.dto.RegisterRequest;

public interface AuthService {

    AuthResponse registerCustomer(RegisterRequest request);

    AuthResponse registerBusinessOwner(RegisterRequest request);

    AuthResponse loginCustomer(LoginRequest request);

    AuthResponse loginBusinessOwner(LoginRequest request);
}
