package org.fastappointment.service;

import org.fastappointment.api.dto.BookingResponse;
import org.fastappointment.api.dto.CreateBookingRequest;
import org.fastappointment.api.dto.CreateBusinessBookingRequest;
import org.fastappointment.api.dto.UpdateBookingRequest;

import java.util.List;

public interface BookingService {

    List<BookingResponse> findAllForCustomer(Long userId);

    BookingResponse createForCustomer(Long userId, CreateBookingRequest request);

    BookingResponse updateForCustomer(Long userId, Long bookingId, UpdateBookingRequest request);

    void cancelForCustomer(Long userId, Long bookingId);

    List<BookingResponse> findAllForOwner(Long ownerId);

    BookingResponse createForOwner(Long ownerId, CreateBusinessBookingRequest request);

    BookingResponse updateForOwner(Long ownerId, Long bookingId, UpdateBookingRequest request);

    void cancelForOwner(Long ownerId, Long bookingId);
}
