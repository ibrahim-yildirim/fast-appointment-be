package org.fastappointment.api.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.fastappointment.util.PhoneNumbers;

import java.util.List;

public class CreateBusinessRequest {

    @NotBlank
    @Size(max = 64)
    @Pattern(regexp = "^[a-z0-9]+(-[a-z0-9]+)*$", message = "{error.business.slugFormat}")
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
    @Size(max = 60)
    private String city;

    @NotBlank
    @Pattern(regexp = PhoneNumbers.BUSINESS_PATTERN, message = "{error.validation.businessPhone}")
    private String phone;

    @NotBlank
    @Email
    @Size(max = 255)
    private String email;

    @NotNull
    @Size(min = 7, max = 7)
    @Valid
    private List<BusinessHoursRequest> hours;

    @AssertTrue(message = "{error.hours.duplicateDay}")
    public boolean isEveryDayOnce() {
        if (hours == null) {
            return true;
        }
        long distinctDays = hours.stream()
                .map(BusinessHoursRequest::getDay)
                .distinct()
                .count();
        return distinctDays == hours.size();
    }

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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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

    public List<BusinessHoursRequest> getHours() {
        return hours;
    }

    public void setHours(List<BusinessHoursRequest> hours) {
        this.hours = hours;
    }
}
