package org.fastappointment.api;

import jakarta.validation.Valid;
import org.fastappointment.api.dto.BookingResponse;
import org.fastappointment.api.dto.CreateBookingRequest;
import org.fastappointment.api.dto.CreateBusinessBookingRequest;
import org.fastappointment.api.dto.CustomerBookingResponse;
import org.fastappointment.api.dto.UpdateBookingRequest;
import org.fastappointment.service.BookingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping("/bookings")
    public ResponseEntity<List<CustomerBookingResponse>> getMyBookings(Authentication authentication) {
        Long userId = Long.valueOf(authentication.getName());
        return ResponseEntity.ok().body(bookingService.findAllForCustomer(userId));
    }

    @PostMapping("/bookings")
    public ResponseEntity<CustomerBookingResponse> createBooking(Authentication authentication,
                                                                 @Valid @RequestBody CreateBookingRequest request) {
        Long userId = Long.valueOf(authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(bookingService.createForCustomer(userId, request));
    }

    @PutMapping("/bookings/{id}")
    public ResponseEntity<CustomerBookingResponse> updateBooking(Authentication authentication, @PathVariable Long id,
                                                                 @Valid @RequestBody UpdateBookingRequest request) {
        Long userId = Long.valueOf(authentication.getName());
        return ResponseEntity.ok().body(bookingService.updateForCustomer(userId, id, request));
    }

    @DeleteMapping("/bookings/{id}")
    public ResponseEntity<Void> cancelBooking(Authentication authentication, @PathVariable Long id) {
        Long userId = Long.valueOf(authentication.getName());
        bookingService.cancelForCustomer(userId, id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/business/bookings")
    public ResponseEntity<List<BookingResponse>> getBusinessBookings(Authentication authentication) {
        Long ownerId = Long.valueOf(authentication.getName());
        return ResponseEntity.ok().body(bookingService.findAllForOwner(ownerId));
    }

    @PostMapping("/business/bookings")
    public ResponseEntity<BookingResponse> createBusinessBooking(Authentication authentication,
                                                                 @Valid @RequestBody CreateBusinessBookingRequest request) {
        Long ownerId = Long.valueOf(authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(bookingService.createForOwner(ownerId, request));
    }

    @PutMapping("/business/bookings/{id}")
    public ResponseEntity<BookingResponse> updateBusinessBooking(Authentication authentication, @PathVariable Long id,
                                                                 @Valid @RequestBody UpdateBookingRequest request) {
        Long ownerId = Long.valueOf(authentication.getName());
        return ResponseEntity.ok().body(bookingService.updateForOwner(ownerId, id, request));
    }

    @DeleteMapping("/business/bookings/{id}")
    public ResponseEntity<Void> cancelBusinessBooking(Authentication authentication, @PathVariable Long id) {
        Long ownerId = Long.valueOf(authentication.getName());
        bookingService.cancelForOwner(ownerId, id);
        return ResponseEntity.noContent().build();
    }
}
