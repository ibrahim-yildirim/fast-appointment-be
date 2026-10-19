package org.fastappointment.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class CreateBusinessRequest {

    @NotBlank
    @Size(max = 64)
    @Pattern(regexp = "^[a-z0-9]+(-[a-z0-9]+)*$", message = "must be lowercase letters, digits and single hyphens")
    private String slug;

    @NotBlank
    @Size(max = 120)
    private String name;

    @NotBlank
    private String description;

    @NotBlank
    @Size(max = 64)
    private String timezone;

    @NotBlank
    @Size(max = 255)
    private String address;

    @NotBlank
    @Size(max = 32)
    private String phone;

    @NotBlank
    @Email
    @Size(max = 255)
    private String email;

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
