package org.fastappointment.service;

import org.fastappointment.api.dto.BookingResponse;
import org.fastappointment.api.dto.CancelBookingRequest;
import org.fastappointment.api.dto.CreateBookingRequest;
import org.fastappointment.api.dto.CreateBusinessBookingRequest;
import org.fastappointment.api.dto.CreateGuestBookingRequest;
import org.fastappointment.api.dto.CustomerBookingResponse;
import org.fastappointment.api.dto.UpdateBookingRequest;

import java.util.List;
import java.util.UUID;

public interface BookingService {

    List<CustomerBookingResponse> findAllForCustomer(Long userId);

    CustomerBookingResponse createForCustomer(Long userId, CreateBookingRequest request);

    CustomerBookingResponse updateForCustomer(Long userId, Long bookingId, UpdateBookingRequest request);

    void cancelForCustomer(Long userId, Long bookingId, CancelBookingRequest request);

    List<BookingResponse> findAllForOwner(Long ownerId);

    BookingResponse createForOwner(Long ownerId, CreateBusinessBookingRequest request);

    BookingResponse updateForOwner(Long ownerId, Long bookingId, UpdateBookingRequest request);

    void cancelForOwner(Long ownerId, Long bookingId, CancelBookingRequest request);

    BookingResponse markNoShowForOwner(Long ownerId, Long bookingId);

    CustomerBookingResponse createForGuest(String slug, CreateGuestBookingRequest request);

    CustomerBookingResponse findByUid(UUID uid);

    void cancelByUid(UUID uid, CancelBookingRequest request);
}
