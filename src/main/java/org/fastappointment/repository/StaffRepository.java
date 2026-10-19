package org.fastappointment.repository;

import org.fastappointment.entity.concretes.Staff;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StaffRepository extends JpaRepository<Staff, Long> {
}
