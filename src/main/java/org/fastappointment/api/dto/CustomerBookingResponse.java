package org.fastappointment.api.dto;

import org.fastappointment.entity.enums.BookingStatus;
import org.fastappointment.entity.enums.CancelledBy;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CustomerBookingResponse {

    private final Long id;
    private final Long businessId;
    private final String businessName;
    private final String businessSlug;
    private final Long serviceId;
    private final String serviceName;
    private final Long staffId;
    private final String staffName;
    private final LocalDateTime startsAt;
    private final LocalDateTime endsAt;
    private final BookingStatus status;
    private final BigDecimal price;
    private final String notes;
    private final CancelledBy cancelledBy;
    private final String cancellationReason;

    public CustomerBookingResponse(Long id, Long businessId, String businessName, String businessSlug,
                                   Long serviceId, String serviceName, Long staffId, String staffName,
                                   LocalDateTime startsAt, LocalDateTime endsAt, BookingStatus status,
                                   BigDecimal price, String notes, CancelledBy cancelledBy,
                                   String cancellationReason) {
        this.id = id;
        this.businessId = businessId;
        this.businessName = businessName;
        this.businessSlug = businessSlug;
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.staffId = staffId;
        this.staffName = staffName;
        this.startsAt = startsAt;
        this.endsAt = endsAt;
        this.status = status;
        this.price = price;
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

    public String getBusinessName() {
        return businessName;
    }

    public String getBusinessSlug() {
        return businessSlug;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public Long getStaffId() {
        return staffId;
    }

    public String getStaffName() {
        return staffName;
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

    public BigDecimal getPrice() {
        return price;
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
