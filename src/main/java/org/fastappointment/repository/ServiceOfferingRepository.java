package org.fastappointment.repository;

import org.fastappointment.entity.concretes.ServiceOffering;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ServiceOfferingRepository extends JpaRepository<ServiceOffering, Long> {

    @EntityGraph(attributePaths = "eligibleStaff")
    List<ServiceOffering> findByBusinessIdAndActiveTrue(Long businessId);

    @EntityGraph(attributePaths = "eligibleStaff")
    List<ServiceOffering> findByBusinessIdOrderByIdAsc(Long businessId);

    List<ServiceOffering> findByBusinessIdAndIdIn(Long businessId, Collection<Long> ids);

    Optional<ServiceOffering> findByIdAndBusinessId(Long id, Long businessId);

    Optional<ServiceOffering> findByIdAndBusinessIdAndActiveTrue(Long id, Long businessId);
}
