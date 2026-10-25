package org.fastappointment.service.impl;

import org.fastappointment.api.dto.AuthResponse;
import org.fastappointment.api.dto.LoginRequest;
import org.fastappointment.api.dto.RefreshTokenRequest;
import org.fastappointment.api.dto.RegisterRequest;
import org.fastappointment.entity.concretes.Business;
import org.fastappointment.entity.concretes.Customer;
import org.fastappointment.entity.concretes.User;
import org.fastappointment.entity.enums.Role;
import org.fastappointment.exception.ConflictException;
import org.fastappointment.exception.ErrorMessageKeys;
import org.fastappointment.exception.UnauthorizedException;
import org.fastappointment.repository.BusinessRepository;
import org.fastappointment.repository.CustomerRepository;
import org.fastappointment.repository.UserRepository;
import org.fastappointment.service.AuthService;
import org.fastappointment.service.TokenService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final BusinessRepository businessRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public AuthServiceImpl(UserRepository userRepository, CustomerRepository customerRepository,
                           BusinessRepository businessRepository, PasswordEncoder passwordEncoder,
                           TokenService tokenService) {
        this.userRepository = userRepository;
        this.customerRepository = customerRepository;
        this.businessRepository = businessRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    @Override
    @Transactional
    public AuthResponse registerCustomer(RegisterRequest request) {
        User user = createUser(request, Role.CUSTOMER);
        Customer customer = Customer.builder()
                .user(user)
                .name(user.getName())
                .email(user.getEmail())
                .build();
        Customer savedCustomer = customerRepository.save(customer);
        return toAuthResponse(user, savedCustomer.getId(), null);
    }

    @Override
    @Transactional
    public AuthResponse registerBusinessOwner(RegisterRequest request) {
        User user = createUser(request, Role.BUSINESS_OWNER);
        return toAuthResponse(user, user.getId(), null);
    }

    @Override
    public AuthResponse loginCustomer(LoginRequest request) {
        return customerAuthResponse(authenticate(request, Role.CUSTOMER));
    }

    @Override
    public AuthResponse loginBusinessOwner(LoginRequest request) {
        return businessOwnerAuthResponse(authenticate(request, Role.BUSINESS_OWNER));
    }

    @Override
    @Transactional(noRollbackFor = UnauthorizedException.class)
    public AuthResponse refresh(RefreshTokenRequest request) {
        User user = tokenService.consumeRefreshToken(request.getRefreshToken());
        return switch (user.getRole()) {
            case CUSTOMER -> customerAuthResponse(user);
            case BUSINESS_OWNER -> businessOwnerAuthResponse(user);
        };
    }

    @Override
    @Transactional
    public void logout(RefreshTokenRequest request) {
        tokenService.revokeRefreshToken(request.getRefreshToken());
    }

    private AuthResponse customerAuthResponse(User user) {
        Customer customer = customerRepository.findByUserId(user.getId())
                .orElseThrow(() -> new UnauthorizedException(ErrorMessageKeys.INVALID_CREDENTIALS));
        return toAuthResponse(user, customer.getId(), null);
    }

    private AuthResponse businessOwnerAuthResponse(User user) {
        Long businessId = businessRepository.findByOwnerId(user.getId())
                .map(Business::getId)
                .orElse(null);
        return toAuthResponse(user, user.getId(), businessId);
    }

    private User createUser(RegisterRequest request, Role role) {
        String email = normalizeEmail(request.getEmail());
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

    private User authenticate(LoginRequest request, Role expectedRole) {
        User user = userRepository.findByEmail(normalizeEmail(request.getEmail()))
                .orElseThrow(() -> new UnauthorizedException(ErrorMessageKeys.INVALID_CREDENTIALS));
        boolean passwordMatches = passwordEncoder.matches(request.getPassword(), user.getPasswordHash());
        if (!passwordMatches || user.getRole() != expectedRole) {
            throw new UnauthorizedException(ErrorMessageKeys.INVALID_CREDENTIALS);
        }
        return user;
    }

    private AuthResponse toAuthResponse(User user, Long actorId, Long businessId) {
        String token = tokenService.generateToken(user);
        String refreshToken = tokenService.createRefreshToken(user);
        return new AuthResponse(token, refreshToken, user.getRole(), actorId, businessId);
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase();
    }
}
