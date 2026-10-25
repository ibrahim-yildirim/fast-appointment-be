package org.fastappointment.api.dto;

import org.fastappointment.entity.enums.Role;

public class AuthResponse {

    private final String token;
    private final String refreshToken;
    private final Role role;
    private final Long actorId;
    private final Long businessId;

    public AuthResponse(String token, String refreshToken, Role role, Long actorId, Long businessId) {
        this.token = token;
        this.refreshToken = refreshToken;
        this.role = role;
        this.actorId = actorId;
        this.businessId = businessId;
    }

    public String getToken() {
        return token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public Role getRole() {
        return role;
    }

    public Long getActorId() {
        return actorId;
    }

    public Long getBusinessId() {
        return businessId;
    }
}
