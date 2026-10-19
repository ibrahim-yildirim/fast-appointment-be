package org.fastappointment.service.impl;

import org.fastappointment.api.dto.RegisterRequest;
import org.fastappointment.api.dto.UserResponse;
import org.fastappointment.entity.concretes.Customer;
import org.fastappointment.entity.concretes.User;
import org.fastappointment.entity.enums.Role;
import org.fastappointment.exception.ConflictException;
import org.fastappointment.exception.ErrorMessageKeys;
import org.fastappointment.mapper.UserEntityToUserResponse;
import org.fastappointment.repository.CustomerRepository;
import org.fastappointment.repository.UserRepository;
import org.fastappointment.service.AuthService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserEntityToUserResponse userEntityToUserResponse;

    public AuthServiceImpl(UserRepository userRepository, CustomerRepository customerRepository,
                           PasswordEncoder passwordEncoder, UserEntityToUserResponse userEntityToUserResponse) {
        this.userRepository = userRepository;
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
        this.userEntityToUserResponse = userEntityToUserResponse;
    }

    @Override
    @Transactional
    public UserResponse registerCustomer(RegisterRequest request) {
        User user = createUser(request, Role.CUSTOMER);
        Customer customer = Customer.builder()
                .user(user)
                .name(user.getName())
                .email(user.getEmail())
                .build();
        customerRepository.save(customer);
        return userEntityToUserResponse.toResponse(user);
    }

    @Override
    @Transactional
    public UserResponse registerBusinessOwner(RegisterRequest request) {
        User user = createUser(request, Role.BUSINESS_OWNER);
        return userEntityToUserResponse.toResponse(user);
    }

    private User createUser(RegisterRequest request, Role role) {
        String email = request.getEmail().trim().toLowerCase();
        if (userRepository.existsByEmail(email)) {
            throw new ConflictException(ErrorMessageKeys.EMAIL_TAKEN, email);
        }
        User user = User.builder()
                .email(email)
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .role(role)
                .build();
        return userRepository.save(user);
    }
}
