package org.fastappointment.mapper;

import org.fastappointment.api.dto.CustomerBookingResponse;
import org.fastappointment.entity.concretes.Booking;
import org.springframework.stereotype.Component;

@Component
public class BookingEntityToCustomerBookingResponse {

    public CustomerBookingResponse toResponse(Booking booking) {
        Long serviceId = null;
        if (booking.getService() != null) {
            serviceId = booking.getService().getId();
        }
        return new CustomerBookingResponse(
                booking.getId(),
                booking.getUid(),
                booking.getBusiness().getId(),
                booking.getBusiness().getName(),
                booking.getBusiness().getSlug(),
                serviceId,
                booking.getServiceName(),
                booking.getStaff().getId(),
                booking.getStaff().getName(),
                booking.getStartsAt(),
                booking.getEndsAt(),
                booking.getStatus(),
                booking.getServicePrice(),
                booking.getNotes(),
                booking.getCancelledBy(),
                booking.getCancellationReason()
        );
    }
}
