package org.fastappointment.api.dto;

import org.fastappointment.entity.enums.Role;

public class AuthResponse {

    private final String token;
    private final Role role;
    private final Long actorId;
    private final Long businessId;

    public AuthResponse(String token, Role role, Long actorId, Long businessId) {
        this.token = token;
        this.role = role;
        this.actorId = actorId;
        this.businessId = businessId;
    }

    public String getToken() {
        return token;
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
