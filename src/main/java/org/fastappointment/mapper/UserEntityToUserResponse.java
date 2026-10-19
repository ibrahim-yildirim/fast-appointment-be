package org.fastappointment.mapper;

import org.fastappointment.api.dto.UserResponse;
import org.fastappointment.entity.concretes.User;
import org.springframework.stereotype.Component;

@Component
public class UserEntityToUserResponse {

    public UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole()
        );
    }
}
