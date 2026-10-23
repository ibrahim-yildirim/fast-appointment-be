package org.fastappointment.repository;

import org.fastappointment.entity.concretes.Staff;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StaffRepository extends JpaRepository<Staff, Long> {

    Optional<Staff> findByIdAndBusinessId(Long id, Long businessId);
}
