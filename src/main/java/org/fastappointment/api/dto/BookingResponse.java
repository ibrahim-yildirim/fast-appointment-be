package org.fastappointment.api.dto;

import org.fastappointment.entity.enums.BookingStatus;

import java.time.LocalDateTime;

public class BookingResponse {

    private final Long id;
    private final Long businessId;
    private final Long serviceId;
    private final Long staffId;
    private final Long customerId;
    private final LocalDateTime startsAt;
    private final LocalDateTime endsAt;
    private final BookingStatus status;
    private final String notes;

    public BookingResponse(Long id, Long businessId, Long serviceId, Long staffId, Long customerId,
                           LocalDateTime startsAt, LocalDateTime endsAt, BookingStatus status, String notes) {
        this.id = id;
        this.businessId = businessId;
        this.serviceId = serviceId;
        this.staffId = staffId;
        this.customerId = customerId;
        this.startsAt = startsAt;
        this.endsAt = endsAt;
        this.status = status;
        this.notes = notes;
    }

    public Long getId() {
        return id;
    }

    public Long getBusinessId() {
        return businessId;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public Long getStaffId() {
        return staffId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public LocalDateTime getStartsAt() {
        return startsAt;
    }

    public LocalDateTime getEndsAt() {
        return endsAt;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public String getNotes() {
        return notes;
    }
}
