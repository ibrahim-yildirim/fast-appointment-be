package org.fastappointment.service.impl;

import org.fastappointment.config.JwtProperties;
import org.fastappointment.entity.concretes.RefreshToken;
import org.fastappointment.entity.concretes.User;
import org.fastappointment.exception.ErrorMessageKeys;
import org.fastappointment.exception.UnauthorizedException;
import org.fastappointment.repository.RefreshTokenRepository;
import org.fastappointment.service.TokenService;
import org.fastappointment.util.AppConstants;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HexFormat;

@Service
public class TokenServiceImpl implements TokenService {

    private final JwtProperties jwtProperties;
    private final JwtEncoder jwtEncoder;
    private final RefreshTokenRepository refreshTokenRepository;
    private final SecureRandom secureRandom = new SecureRandom();

    public TokenServiceImpl(JwtProperties jwtProperties, JwtEncoder jwtEncoder,
                            RefreshTokenRepository refreshTokenRepository) {
        this.jwtProperties = jwtProperties;
        this.jwtEncoder = jwtEncoder;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public String generateToken(User user) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(String.valueOf(user.getId()))
                .issuedAt(now)
                .expiresAt(now.plus(jwtProperties.getExpiration()))
                .claim("email", user.getEmail())
                .claim("role", user.getRole().name())
                .build();
        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();
        return jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
    }

    @Override
    public String createRefreshToken(User user) {
        byte[] bytes = new byte[AppConstants.REFRESH_TOKEN_BYTES];
        secureRandom.nextBytes(bytes);
        String rawToken = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .tokenHash(hash(rawToken))
                .expiresAt(LocalDateTime.now().plus(jwtProperties.getRefreshExpiration()))
                .build();
        refreshTokenRepository.save(refreshToken);
        return rawToken;
    }

    @Override
    public User consumeRefreshToken(String rawToken) {
        RefreshToken refreshToken = refreshTokenRepository.findByTokenHash(hash(rawToken))
                .orElseThrow(() -> new UnauthorizedException(ErrorMessageKeys.INVALID_REFRESH_TOKEN));
        LocalDateTime now = LocalDateTime.now();
        if (refreshToken.isRevoked()) {
            refreshTokenRepository.revokeAllForUser(refreshToken.getUser().getId(), now);
            throw new UnauthorizedException(ErrorMessageKeys.INVALID_REFRESH_TOKEN);
        }
        if (refreshToken.isExpired(now)) {
            throw new UnauthorizedException(ErrorMessageKeys.INVALID_REFRESH_TOKEN);
        }
        refreshToken.revoke(now);
        return refreshToken.getUser();
    }

    @Override
    public void revokeRefreshToken(String rawToken) {
        refreshTokenRepository.findByTokenHash(hash(rawToken))
                .filter(refreshToken -> !refreshToken.isRevoked())
                .ifPresent(refreshToken -> refreshToken.revoke(LocalDateTime.now()));
    }

    private String hash(String rawToken) {
        try {
            byte[] digest = MessageDigest.getInstance("SHA-256").digest(rawToken.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }
}
