package org.fastappointment.api.dto;

import java.util.List;

public class StaffResponse {

    private final Long id;
    private final Long businessId;
    private final String name;
    private final String email;
    private final String phone;
    private final String colorTag;
    private final Boolean active;
    private final List<Long> serviceIds;

    public StaffResponse(Long id, Long businessId, String name, String email, String phone, String colorTag,
                         Boolean active, List<Long> serviceIds) {
        this.id = id;
        this.businessId = businessId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.colorTag = colorTag;
        this.active = active;
        this.serviceIds = serviceIds;
    }

    public Long getId() {
        return id;
    }

    public Long getBusinessId() {
        return businessId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getColorTag() {
        return colorTag;
    }

    public Boolean getActive() {
        return active;
    }

    public List<Long> getServiceIds() {
        return serviceIds;
    }
}
