package org.fastappointment.repository;

import org.fastappointment.entity.concretes.BusinessHours;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BusinessHoursRepository extends JpaRepository<BusinessHours, Long> {

    List<BusinessHours> findByBusinessId(Long businessId);

    List<BusinessHours> findByBusinessIdIn(List<Long> businessIds);
}
