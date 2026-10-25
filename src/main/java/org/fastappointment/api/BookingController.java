package org.fastappointment.api;

import jakarta.validation.Valid;
import org.fastappointment.api.dto.BookingResponse;
import org.fastappointment.api.dto.CancelBookingRequest;
import org.fastappointment.api.dto.CreateBookingRequest;
import org.fastappointment.api.dto.CreateBusinessBookingRequest;
import org.fastappointment.api.dto.CreateGuestBookingRequest;
import org.fastappointment.api.dto.CustomerBookingResponse;
import org.fastappointment.api.dto.UpdateBookingRequest;
import org.fastappointment.service.BookingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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
    public ResponseEntity<Void> cancelBooking(Authentication authentication, @PathVariable Long id,
                                              @Valid @RequestBody(required = false) CancelBookingRequest request) {
        Long userId = Long.valueOf(authentication.getName());
        bookingService.cancelForCustomer(userId, id, request);
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
    public ResponseEntity<Void> cancelBusinessBooking(Authentication authentication, @PathVariable Long id,
                                                      @Valid @RequestBody(required = false) CancelBookingRequest request) {
        Long ownerId = Long.valueOf(authentication.getName());
        bookingService.cancelForOwner(ownerId, id, request);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/business/bookings/{id}/no-show")
    public ResponseEntity<BookingResponse> markNoShow(Authentication authentication, @PathVariable Long id) {
        Long ownerId = Long.valueOf(authentication.getName());
        return ResponseEntity.ok().body(bookingService.markNoShowForOwner(ownerId, id));
    }

    @PostMapping("/businesses/{slug}/bookings")
    public ResponseEntity<CustomerBookingResponse> createGuestBooking(@PathVariable String slug,
                                                                      @Valid @RequestBody CreateGuestBookingRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookingService.createForGuest(slug, request));
    }

    @GetMapping("/bookings/manage/{uid}")
    public ResponseEntity<CustomerBookingResponse> getGuestBooking(@PathVariable UUID uid) {
        return ResponseEntity.ok(bookingService.findByUid(uid));
    }

    @DeleteMapping("/bookings/manage/{uid}")
    public ResponseEntity<Void> cancelGuestBooking(@PathVariable UUID uid,
                                                   @Valid @RequestBody(required = false) CancelBookingRequest request) {
        bookingService.cancelByUid(uid, request);
        return ResponseEntity.noContent().build();
    }
}
