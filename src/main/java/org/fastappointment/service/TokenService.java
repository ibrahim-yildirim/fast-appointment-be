package org.fastappointment.service;

import org.fastappointment.entity.concretes.User;

public interface TokenService {

    String generateToken(User user);

}
