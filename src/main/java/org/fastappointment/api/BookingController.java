package org.fastappointment.api;

import org.fastappointment.api.dto.BookingDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @GetMapping
    public ResponseEntity<List<BookingDto>> getAllBookings() {
        return ResponseEntity.ok().body(new ArrayList<>());
    }

    @PostMapping
    public BookingDto createBooking(BookingDto booking) {
        return booking;
    }

    @DeleteMapping("/{id}")
    public void deleteBooking(@PathVariable Long id) {
    }
}
