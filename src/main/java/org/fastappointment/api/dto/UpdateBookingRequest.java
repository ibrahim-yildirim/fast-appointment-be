package org.fastappointment.api.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class UpdateBookingRequest {

    @NotNull
    private Long serviceId;

    @NotNull
    private Long staffId;

    @NotNull
    private LocalDateTime startsAt;

    @NotNull
    private LocalDateTime endsAt;

    private String notes;

    @AssertTrue(message = "{error.booking.endsBeforeStarts}")
    public boolean isEndsAfterStarts() {
        if (startsAt == null || endsAt == null) {
            return true;
        }
        return endsAt.isAfter(startsAt);
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

    public LocalDateTime getEndsAt() {
        return endsAt;
    }

    public void setEndsAt(LocalDateTime endsAt) {
        this.endsAt = endsAt;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
