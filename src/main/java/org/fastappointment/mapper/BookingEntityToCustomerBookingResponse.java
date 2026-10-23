package org.fastappointment.mapper;

import org.fastappointment.api.dto.CustomerBookingResponse;
import org.fastappointment.entity.concretes.Booking;
import org.springframework.stereotype.Component;

@Component
public class BookingEntityToCustomerBookingResponse {

    public CustomerBookingResponse toResponse(Booking booking) {
        return new CustomerBookingResponse(
                booking.getId(),
                booking.getBusiness().getId(),
                booking.getBusiness().getName(),
                booking.getBusiness().getSlug(),
                booking.getService().getId(),
                booking.getService().getName(),
                booking.getStaff().getId(),
                booking.getStaff().getName(),
                booking.getStartsAt(),
                booking.getEndsAt(),
                booking.getStatus(),
                booking.getService().getPrice(),
                booking.getNotes()
        );
    }
}
