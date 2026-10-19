package org.fastappointment.service;

import org.fastappointment.api.dto.RegisterRequest;
import org.fastappointment.api.dto.UserResponse;

public interface AuthService {

    UserResponse registerCustomer(RegisterRequest request);

    UserResponse registerBusinessOwner(RegisterRequest request);
}
