package org.fastappointment.api.dto;

import org.fastappointment.entity.enums.BookingStatus;
import org.fastappointment.entity.enums.CancelledBy;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class BookingResponse {

    private final Long id;
    private final Long businessId;
    private final Long serviceId;
    private final String serviceName;
    private final BigDecimal servicePrice;
    private final Long staffId;
    private final Long customerId;
    private final LocalDateTime startsAt;
    private final LocalDateTime endsAt;
    private final BookingStatus status;
    private final String notes;
    private final CancelledBy cancelledBy;
    private final String cancellationReason;

    public BookingResponse(Long id, Long businessId, Long serviceId, String serviceName, BigDecimal servicePrice,
                           Long staffId, Long customerId,
                           LocalDateTime startsAt, LocalDateTime endsAt, BookingStatus status, String notes,
                           CancelledBy cancelledBy, String cancellationReason) {
        this.id = id;
        this.businessId = businessId;
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.servicePrice = servicePrice;
        this.staffId = staffId;
        this.customerId = customerId;
        this.startsAt = startsAt;
        this.endsAt = endsAt;
        this.status = status;
        this.notes = notes;
        this.cancelledBy = cancelledBy;
        this.cancellationReason = cancellationReason;
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

    public String getServiceName() {
        return serviceName;
    }

    public BigDecimal getServicePrice() {
        return servicePrice;
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

    public CancelledBy getCancelledBy() {
        return cancelledBy;
    }

    public String getCancellationReason() {
        return cancellationReason;
    }
}
