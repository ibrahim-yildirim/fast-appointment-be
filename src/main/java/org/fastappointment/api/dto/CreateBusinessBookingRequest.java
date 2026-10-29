package org.fastappointment.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.fastappointment.util.PhoneNumbers;

import java.time.LocalDateTime;

public class CreateBusinessBookingRequest {

    @NotNull
    private Long serviceId;

    @NotNull
    private Long staffId;

    @NotBlank
    @Size(max = 120)
    private String customerName;

    @NotBlank
    @Pattern(regexp = PhoneNumbers.MOBILE_PATTERN, message = "{error.validation.mobilePhone}")
    private String customerPhone;

    @Email
    @Size(max = 255)
    private String customerEmail;

    @NotNull
    private LocalDateTime startsAt;

    private String notes;

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public Long getStaffId() {
        return staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public LocalDateTime getStartsAt() {
        return startsAt;
    }

    public void setStartsAt(LocalDateTime startsAt) {
        this.startsAt = startsAt;
    }


    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
