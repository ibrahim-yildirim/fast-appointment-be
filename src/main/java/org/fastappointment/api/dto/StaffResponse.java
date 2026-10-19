package org.fastappointment.api.dto;

public class StaffResponse {

    private final Long id;
    private final Long businessId;
    private final String name;
    private final String email;
    private final String colorTag;

    public StaffResponse(Long id, Long businessId, String name, String email, String colorTag) {
        this.id = id;
        this.businessId = businessId;
        this.name = name;
        this.email = email;
        this.colorTag = colorTag;
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

    public String getColorTag() {
        return colorTag;
    }
}
