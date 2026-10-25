package org.fastappointment.service;

import org.fastappointment.entity.concretes.User;

public interface TokenService {

    String generateToken(User user);

    String createRefreshToken(User user);

    User consumeRefreshToken(String rawToken);

    void revokeRefreshToken(String rawToken);
}
