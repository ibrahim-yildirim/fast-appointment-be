package org.fastappointment.service;

import org.fastappointment.api.dto.BookingResponse;
import org.fastappointment.api.dto.CreateBookingRequest;
import org.fastappointment.api.dto.CreateBusinessBookingRequest;

import java.util.List;

public interface BookingService {

    List<BookingResponse> findAllForCustomer(Long userId);

    BookingResponse createForCustomer(Long userId, CreateBookingRequest request);

    void deleteForCustomer(Long userId, Long bookingId);

    List<BookingResponse> findAllForOwner(Long ownerId);

    BookingResponse createForOwner(Long ownerId, CreateBusinessBookingRequest request);

    void deleteForOwner(Long ownerId, Long bookingId);
}
