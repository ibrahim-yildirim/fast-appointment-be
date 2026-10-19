package org.fastappointment.repository;

import org.fastappointment.entity.concretes.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {



}
