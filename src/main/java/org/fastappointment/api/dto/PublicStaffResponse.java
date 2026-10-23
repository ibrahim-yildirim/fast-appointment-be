package org.fastappointment.api.dto;

public class PublicStaffResponse {

    private final Long id;
    private final String name;
    private final String colorTag;

    public PublicStaffResponse(Long id, String name, String colorTag) {
        this.id = id;
        this.name = name;
        this.colorTag = colorTag;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColorTag() {
        return colorTag;
    }
}
