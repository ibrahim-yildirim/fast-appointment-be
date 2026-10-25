package org.fastappointment.repository;

import org.fastappointment.entity.concretes.Booking;
import org.fastappointment.entity.enums.BookingStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @EntityGraph(attributePaths = {"business", "service", "staff"})
    List<Booking> findByCustomerIdOrderByStartsAtAsc(Long customerId);

    List<Booking> findByBusinessId(Long businessId);

    Optional<Booking> findByIdAndCustomerId(Long id, Long customerId);

    Optional<Booking> findByIdAndBusinessId(Long id, Long businessId);

    @EntityGraph(attributePaths = {"business", "service", "staff"})
    Optional<Booking> findByUid(UUID uid);

    long countByCustomerIdAndStatusAndStartsAtAfter(Long customerId, BookingStatus status, LocalDateTime after);

    @Query("select count(b) > 0 from Booking b where b.staff.id = :staffId and b.status <> :ignoredStatus and b.startsAt < :endsAt and b.endsAt > :startsAt")
    boolean existsOverlappingBooking(@Param("staffId") Long staffId,
                                     @Param("startsAt") LocalDateTime startsAt,
                                     @Param("endsAt") LocalDateTime endsAt,
                                     @Param("ignoredStatus") BookingStatus ignoredStatus);

    @Query("select count(b) > 0 from Booking b where b.customer.id = :customerId and b.status <> :ignoredStatus and b.startsAt < :endsAt and b.endsAt > :startsAt")
    boolean existsOverlappingCustomerBooking(@Param("customerId") Long customerId,
                                             @Param("startsAt") LocalDateTime startsAt,
                                             @Param("endsAt") LocalDateTime endsAt,
                                             @Param("ignoredStatus") BookingStatus ignoredStatus);

    @Query("select count(b) > 0 from Booking b where b.id <> :bookingId and b.customer.id = :customerId and b.status <> :ignoredStatus and b.startsAt < :endsAt and b.endsAt > :startsAt")
    boolean existsOverlappingCustomerBookingExcluding(@Param("bookingId") Long bookingId,
                                                      @Param("customerId") Long customerId,
                                                      @Param("startsAt") LocalDateTime startsAt,
                                                      @Param("endsAt") LocalDateTime endsAt,
                                                      @Param("ignoredStatus") BookingStatus ignoredStatus);

    @Query("select b from Booking b where b.customer.id = :customerId and b.status <> :ignoredStatus and b.startsAt < :to and b.endsAt > :from")
    List<Booking> findActiveForCustomerBetween(@Param("customerId") Long customerId,
                                               @Param("from") LocalDateTime from,
                                               @Param("to") LocalDateTime to,
                                               @Param("ignoredStatus") BookingStatus ignoredStatus);

    @Query("select b from Booking b where b.staff.id in :staffIds and b.status <> :ignoredStatus and b.startsAt < :to and b.endsAt > :from")
    List<Booking> findActiveForStaffBetween(@Param("staffIds") Collection<Long> staffIds,
                                            @Param("from") LocalDateTime from,
                                            @Param("to") LocalDateTime to,
                                            @Param("ignoredStatus") BookingStatus ignoredStatus);

    @Query("select count(b) > 0 from Booking b where b.id <> :bookingId and b.staff.id = :staffId and b.status <> :ignoredStatus and b.startsAt < :endsAt and b.endsAt > :startsAt")
    boolean existsOverlappingBookingExcluding(@Param("bookingId") Long bookingId,
                                              @Param("staffId") Long staffId,
                                              @Param("startsAt") LocalDateTime startsAt,
                                              @Param("endsAt") LocalDateTime endsAt,
                                              @Param("ignoredStatus") BookingStatus ignoredStatus);
}
