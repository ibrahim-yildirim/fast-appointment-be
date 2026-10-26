package org.fastappointment.api.dto;

import java.time.LocalDateTime;
import java.util.List;

public class BusinessResponse {

    private final Long id;
    private final String slug;
    private final String name;
    private final String description;
    private final String timezone;
    private final String address;
    private final String city;
    private final String phone;
    private final String email;
    private final List<BusinessHoursResponse> defaultHours;
    private final LocalDateTime createdAt;

    public BusinessResponse(Long id, String slug, String name, String description, String timezone,
                            String address, String city, String phone, String email, List<BusinessHoursResponse> defaultHours,
                            LocalDateTime createdAt) {
        this.id = id;
        this.slug = slug;
        this.name = name;
        this.description = description;
        this.timezone = timezone;
        this.address = address;
        this.city = city;
        this.phone = phone;
        this.email = email;
        this.defaultHours = defaultHours;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getSlug() {
        return slug;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getTimezone() {
        return timezone;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public List<BusinessHoursResponse> getDefaultHours() {
        return defaultHours;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
