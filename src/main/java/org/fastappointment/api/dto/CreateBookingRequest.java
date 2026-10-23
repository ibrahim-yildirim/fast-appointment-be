package org.fastappointment.api.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class CreateBookingRequest {

    @NotNull
    private Long businessId;

    @NotNull
    private Long serviceId;

    @NotNull
    private Long staffId;

    @NotNull
    private LocalDateTime startsAt;

    private String notes;

    public Long getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Long businessId) {
        this.businessId = businessId;
    }

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
