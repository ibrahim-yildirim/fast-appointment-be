package org.fastappointment.repository;

import org.fastappointment.entity.concretes.ServiceOffering;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ServiceOfferingRepository extends JpaRepository<ServiceOffering, Long> {

    List<ServiceOffering> findByBusinessIdAndActiveTrue(Long businessId);

    Optional<ServiceOffering> findByIdAndBusinessId(Long id, Long businessId);
}
