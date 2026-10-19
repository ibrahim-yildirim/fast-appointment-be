package org.fastappointment.repository;

import org.fastappointment.entity.concretes.ServiceOffering;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceOfferingRepository extends JpaRepository<ServiceOffering, Long> {
}
