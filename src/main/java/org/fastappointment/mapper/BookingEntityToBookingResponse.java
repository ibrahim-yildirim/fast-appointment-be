package org.fastappointment.mapper;

import org.fastappointment.api.dto.BookingResponse;
import org.fastappointment.entity.concretes.Booking;
import org.springframework.stereotype.Component;

@Component
public class BookingEntityToBookingResponse {

    public BookingResponse toResponse(Booking booking) {
        Long serviceId = null;
        if (booking.getService() != null) {
            serviceId = booking.getService().getId();
        }
        Long customerId = null;
        if (booking.getCustomer() != null) {
            customerId = booking.getCustomer().getId();
        }
        return new BookingResponse(
                booking.getId(),
                booking.getBusiness().getId(),
                serviceId,
                booking.getServiceName(),
                booking.getServicePrice(),
                booking.getStaff().getId(),
                customerId,
                booking.getStartsAt(),
                booking.getEndsAt(),
                booking.getStatus(),
                booking.getNotes(),
                booking.getCancelledBy(),
                booking.getCancellationReason()
        );
    }
}
