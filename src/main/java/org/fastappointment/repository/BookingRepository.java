package org.fastappointment.repository;

import org.fastappointment.entity.concretes.Booking;
import org.fastappointment.entity.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByCustomerId(Long customerId);

    List<Booking> findByBusinessId(Long businessId);

    Optional<Booking> findByIdAndCustomerId(Long id, Long customerId);

    Optional<Booking> findByIdAndBusinessId(Long id, Long businessId);

    @Query("select count(b) > 0 from Booking b where b.staff.id = :staffId and b.status <> :ignoredStatus and b.startsAt < :endsAt and b.endsAt > :startsAt")
    boolean existsOverlappingBooking(@Param("staffId") Long staffId,
                                     @Param("startsAt") LocalDateTime startsAt,
                                     @Param("endsAt") LocalDateTime endsAt,
                                     @Param("ignoredStatus") BookingStatus ignoredStatus);

    @Query("select count(b) > 0 from Booking b where b.id <> :bookingId and b.staff.id = :staffId and b.status <> :ignoredStatus and b.startsAt < :endsAt and b.endsAt > :startsAt")
    boolean existsOverlappingBookingExcluding(@Param("bookingId") Long bookingId,
                                              @Param("staffId") Long staffId,
                                              @Param("startsAt") LocalDateTime startsAt,
                                              @Param("endsAt") LocalDateTime endsAt,
                                              @Param("ignoredStatus") BookingStatus ignoredStatus);
}
