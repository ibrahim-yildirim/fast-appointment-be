package org.fastappointment.service;

import org.fastappointment.api.dto.BookingResponse;
import org.fastappointment.api.dto.CreateBookingRequest;

import java.util.List;

public interface BookingService {

    List<BookingResponse> findAll();

    BookingResponse create(CreateBookingRequest request);

    void delete(Long id);
}
